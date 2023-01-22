package service.impl;

import dao.IPaymentDao;
import dao.IUserDao;
import dao.IUserTariffDao;
import dao.impl.PaymentDao;
import dao.impl.UserDaoImpl;
import dao.impl.UserTariffDaoImpl;
import dto.DtoTable;
import dto.DtoUser;
import entity.User;
import entity.UserTariff;
import entity.builder.UserBuilder;
import enums.*;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import service.IEmailService;
import service.ISecurityService;
import service.IUserService;
import service.IValidatorService;
import settings.Regex;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class UserService implements IUserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);


    private static final IUserDao userDao = new UserDaoImpl();
    private static final IUserTariffDao userTariffDao = new UserTariffDaoImpl();
    private static final IPaymentDao paymentDao = new PaymentDao();
    private static final ISecurityService security = new SecurityService();
    private static final IValidatorService validator = new ValidatorService();
    private static final IEmailService emailService = new EmailService();

    public User getUser(String userName, String password) throws DbConnectionException, NoSuchElementException {

        try {
            User user = userDao.getUserByLogin(userName);
            if (security.isPasswordVerify(password, user.getPassword())) return user;
            else throw new NoSuchElementException();
        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e);
        }
    }

    @Override
    public User getLoggedUser(String userName) throws DbConnectionException {
        try {
            return userDao.getUserByLogin(userName);
        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e);
        }
    }
    @Override
    public void setUserStatus(int user, String status) throws DbConnectionException {

        try {
            userDao.setUserStatus(user, status);

            logger.info(String.format("User %s status changed to %s", user, status));

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
    }

    @Override
    public void blockUser(int userId) throws DbConnectionException {
        try {
            List<UserTariff> userTariffList = userTariffDao.getSubscribedUserTariffList(userId);
            for (UserTariff userTariff : userTariffList) {
                int userTariffId = userTariff.getId();
                if (userTariffDao.getUserTariffStatus(userTariffId).equals(SubscribeStatus.ACTIVE)) {
                    LocalDateTime endDate = userTariffDao.getUserTariffEndDate(userTariffId).atStartOfDay();
                    BigDecimal moneyBackPeriod = BigDecimal.valueOf(Duration.between(LocalDate.now().atStartOfDay(), endDate).toDays() - 1);
                    BigDecimal priceForDay = userTariff.getTariff().getPrice().divide(BigDecimal.valueOf(userTariff.getTariff().getPeriod().getDivider()));
                    BigDecimal returnValue = moneyBackPeriod.compareTo(new BigDecimal(0)) > 0 ? priceForDay.multiply(moneyBackPeriod) : new BigDecimal(0);
                    if (returnValue.compareTo(new BigDecimal(0)) > 0)
                        paymentDao.addIncomingPayment(userId, returnValue, IncomingPaymentType.MONEYBACK.getName());
                }
                userTariffDao.setUserTariffStatus(userTariffId, SubscribeStatus.BLOCKED);
            }
            userDao.setUserStatus(userId, UserStatus.BLOCKED.toString());

            logger.info(String.format("User %s blocked ", userId));

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
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
                if (paymentDao.addWithdrawPayment(userId, userTariff.getTariff().getPrice(), userTariffWithdrawDescription)) {
                    userTariffDao.setUserTariffStatus(userTariffId, SubscribeStatus.ACTIVE);
                    userTariffDao.setUserTariffEndDate(userTariffId, date);
                } else
                    userTariffDao.setUserTariffStatus(userTariffId, SubscribeStatus.PAUSED);
            }
            userDao.setUserStatus(userId, UserStatus.ACTIVE.toString());
            logger.info(String.format("User %s unblocked ", userId));

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
    }
    @Override
    public void setUserPassword(int user, String password, String confirm) throws DbConnectionException, IncorrectFormatException {
        validator.validateString(password, Regex.PASSWORD_REGEX, "Incorrect password format");
        validator.validateConfirmPassword(password, confirm, "Password doesn't match");
        try {
            String hashPassword = security.getPasswordHash(password);
            userDao.setUserPassword(user, hashPassword);
            logger.info(String.format("User %s password changed ", user));
        } catch (DbConnectionException e) {
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
            throw new DbConnectionException(e);
        }
    }

    @Override
    public boolean isUserExist(String userName) throws DbConnectionException, NoSuchElementException {

        try {
            userDao.getUserByLogin(userName);
            return true;
        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    @Override
    public User addUser(DtoUser dtoUser) throws IncorrectFormatException, DbConnectionException {

        validator.validateString(dtoUser.getEmail(), Regex.EMAIL_REGEX, "Incorrect Email format");
        validator.validateString(dtoUser.getPassword(), Regex.PASSWORD_REGEX, "Incorrect password format");
        validator.validateString(dtoUser.getName(), Regex.NAME_REGEX, "Incorrect name format");
        validator.validateString(dtoUser.getLastName(), Regex.NAME_REGEX, "Incorrect last name format");
        validator.validateString(dtoUser.getPhone(), Regex.PHONE_NUMBER_REGEX, "Incorrect phone number format");

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

