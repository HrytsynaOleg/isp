package service.impl;

import repository.IPaymentRepository;
import repository.IUserRepository;
import repository.IUserTariffRepository;
import repository.impl.PaymentRepository;
import repository.impl.UserRepositoryImpl;
import repository.impl.UserTariffRepositoryImpl;
import dto.DtoTable;
import dto.DtoUser;
import entity.Payment;
import entity.User;
import entity.UserTariff;
import entity.builder.UserBuilder;
import enums.*;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import exceptions.NotEnoughBalanceException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import service.IEmailService;
import service.ISecurityService;
import service.IUserService;
import service.IValidatorService;
import settings.Regex;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class UserService implements IUserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);


    private static final IUserRepository userDao = new UserRepositoryImpl();
    private static final IUserTariffRepository userTariffDao = new UserTariffRepositoryImpl();
    private static final IPaymentRepository paymentDao = new PaymentRepository();
    private static final ISecurityService security = new SecurityService();
    private static final IValidatorService validator = new ValidatorService();
    private static final IEmailService emailService = new EmailService();

    public User getUser(String userName, String password) throws DbConnectionException, NoSuchElementException {

        try {
            User user = userDao.getUserByLogin(userName);
            if (security.isPasswordVerify(password, user.getPassword())) return user;
            else throw new NoSuchElementException();
        } catch (DbConnectionException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException(e);
        } catch (NoSuchElementException e) {
            logger.error(e.getMessage());
            throw new NoSuchElementException(e);
        }
    }

    @Override
    public User getUserByLogin(String userName) throws DbConnectionException {
        try {
            return userDao.getUserByLogin(userName);
        } catch (DbConnectionException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException(e);
        } catch (NoSuchElementException e) {
            logger.error(e.getMessage());
            throw new NoSuchElementException(e);
        }
    }
    @Override
    public void setUserStatus(int user, String status) throws DbConnectionException {

        try {
            userDao.setUserStatus(user, status);

            logger.info(String.format("User %s status changed to %s", user, status));

        } catch (DbConnectionException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException(e);
        }
    }

    @Override
    public void blockUser(int userId) throws DbConnectionException {
        try {
            User user = userDao.getUserById(userId);
            List<UserTariff> userTariffList = userTariffDao.getSubscribedUserTariffList(userId);
            for (UserTariff userTariff : userTariffList) {
                if (userTariff.getSubscribeStatus().equals(SubscribeStatus.ACTIVE)) {
                    BigDecimal returnValue=userTariff.calcMoneyBackValue();
                    if (returnValue.compareTo(new BigDecimal(0)) > 0) {
                        Payment moneyBackPayment = new Payment(0, user, returnValue, new Date(), PaymentType.IN, IncomingPaymentType.MONEYBACK.getName());
                        paymentDao.addPayment(moneyBackPayment);
                    }
                }
                userTariffDao.setUserTariffStatus(userTariff.getId(), SubscribeStatus.BLOCKED);
            }
            userDao.setUserStatus(userId, UserStatus.BLOCKED.toString());

            logger.info(String.format("User %s blocked ", userId));

        } catch (DbConnectionException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException(e);
        } catch (NotEnoughBalanceException ignored) {

        }
    }

    @Override
    public void unblockUser(int userId) throws DbConnectionException {
        try {

            List<UserTariff> userTariffList = userTariffDao.getBlockedUserTariffList(userId);
            for (UserTariff userTariff : userTariffList) {
                int userTariffId = userTariff.getId();
                LocalDate date = userTariff.getTariff().getPeriod().getNexDate(LocalDate.now());
                String userTariffWithdrawDescription = String.format("%s tariff %s subscribed to %s", userTariff.getTariff().getService().getName(),
                        userTariff.getTariff().getName(), date);
                User user = userDao.getUserById(userId);
                Payment withdraw = new Payment(0, user, userTariff.getTariff().getPrice(), new Date(), PaymentType.OUT, userTariffWithdrawDescription);
                try {
                    paymentDao.addPayment(withdraw);
                    userTariffDao.setUserTariffStatus(userTariffId, SubscribeStatus.ACTIVE);
                    userTariffDao.setUserTariffEndDate(userTariffId, date);
                } catch (NotEnoughBalanceException e) {
                    userTariffDao.setUserTariffStatus(userTariffId, SubscribeStatus.PAUSED);
                }
            }
            userDao.setUserStatus(userId, UserStatus.ACTIVE.toString());
            logger.info(String.format("User %s unblocked ", userId));

        } catch (DbConnectionException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException(e);
        }
    }
    @Override
    public void setUserPassword(int user, String password, String confirm) throws DbConnectionException, IncorrectFormatException {
        validator.validateString(password, Regex.PASSWORD_REGEX, "alert.incorrectPassword");
        validator.validateConfirmPassword(password, confirm, "alert.passwordNotMatch");
        try {
            String hashPassword = security.getPasswordHash(password);
            userDao.setUserPassword(user, hashPassword);
            logger.info(String.format("User %s password changed ", user));
        } catch (DbConnectionException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException(e);
        }
    }

    @Override
    public List<User> getUsersList(DtoTable dtoTable) throws DbConnectionException {
        List<User> users;

        try {
            Map<String,String> parameters = dtoTable.buildQueryParameters();
            users = userDao.getUsersList(parameters);

        } catch (DbConnectionException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException(e);
        }
        return users;
    }
    @Override
    public Integer getUsersCount(DtoTable dtoTable) throws DbConnectionException {
        try {
            Map<String,String> parameters = dtoTable.buildQueryParameters();
            return userDao.getUsersCount(parameters);

        } catch (DbConnectionException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException(e);
        }
    }

    @Override
    public Integer getTotalUsersCount() throws DbConnectionException {
        try {
            return userDao.getUsersCount(null);
        } catch (DbConnectionException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException(e);
        }
    }

    @Override
    public boolean isUserExist(String userName) throws DbConnectionException, NoSuchElementException {

        try {
            userDao.getUserByLogin(userName);
            return true;
        } catch (DbConnectionException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException(e);
        } catch (NoSuchElementException e) {
            logger.error(e.getMessage());
            return false;
        }
    }
    @Override
    public User addUser(DtoUser dtoUser) throws IncorrectFormatException, DbConnectionException {

        validator.validateString(dtoUser.getEmail(), Regex.EMAIL_REGEX, "alert.incorrectEmail");
        validator.validateString(dtoUser.getPassword(), Regex.PASSWORD_REGEX, "alert.incorrectPassword");
        validator.validateString(dtoUser.getName(), Regex.NAME_REGEX, "alert.incorrectName");
        validator.validateString(dtoUser.getLastName(), Regex.NAME_REGEX, "alert.incorrectLastName");
        validator.validateString(dtoUser.getPhone(), Regex.PHONE_NUMBER_REGEX, "alert.incorrectPhone");

        User user = mapDtoToUser(dtoUser);
        String hashPassword = security.getPasswordHash(dtoUser.getPassword());
        user.setPassword(hashPassword);
        user.setRegistration(new Date());
        user.setBalance(BigDecimal.valueOf(0));
        try {
            int userId = userDao.addUser(user);
            user.setId(userId);
            emailService.sendEmail(user.getEmail(), "ISP Registration", "Your password: " + dtoUser.getPassword());
        } catch (DbConnectionException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException(e);
        }
        logger.info(String.format("User %s created ", dtoUser.getEmail()));
        return user;
    }
    @Override
    public User updateUser(DtoUser dtoUser) throws DbConnectionException, IncorrectFormatException {

        validator.validateString(dtoUser.getEmail(), Regex.EMAIL_REGEX, "alert.incorrectEmailFormat");
        validator.validateString(dtoUser.getName(), Regex.NAME_REGEX, "alert.incorrectNameFormat");
        validator.validateString(dtoUser.getLastName(), Regex.NAME_REGEX, "alert.incorrectLastNameFormat");
        validator.validateString(dtoUser.getPhone(), Regex.PHONE_NUMBER_REGEX, "alert.incorrectPhoneFormat");

        User user;
        try {
            userDao.updateUserProfile(dtoUser);
            user = userDao.getUserByLogin(dtoUser.getEmail());
            logger.info(String.format("User %s updated ", dtoUser.getEmail()));
        } catch (DbConnectionException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException(e);
        }
        return user;
    }

    private User mapDtoToUser(DtoUser dtoUser) {
        UserBuilder builder = new UserBuilder();
        builder.setUserEmail(dtoUser.getEmail());
        builder.setUserPassword(dtoUser.getPassword());
        builder.setUserName(dtoUser.getName());
        builder.setUserLastName(dtoUser.getLastName());
        builder.setUserPhone(dtoUser.getPhone());
        builder.setUserAdress(dtoUser.getAddress());
        builder.setUserRole(UserRole.valueOf(dtoUser.getRole()));

        return builder.build();
    }

}

