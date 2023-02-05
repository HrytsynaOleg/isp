package service.impl;

import repository.IPaymentRepository;
import repository.IUserRepository;
import repository.IUserTariffRepository;
import dto.DtoTable;
import dto.DtoUser;
import entity.Payment;
import entity.User;
import entity.UserTariff;
import enums.*;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import exceptions.NotEnoughBalanceException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import service.*;
import settings.Regex;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class UserService implements IUserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);


    private final IUserRepository userRepo;
    private final IUserTariffRepository userTariffRepo;
    private final IPaymentRepository paymentRepo;
    private static final ISecurityService security = new SecurityService();
    private static final IValidatorService validator = new ValidatorService();
    private static final IEmailService emailService = new EmailService();

    public UserService(IUserRepository userRepo, IUserTariffRepository userTariffRepo, IPaymentRepository paymentRepo) {
        this.userRepo = userRepo;
        this.userTariffRepo = userTariffRepo;
        this.paymentRepo = paymentRepo;
    }

    public User getUser(String userName, String password) throws DbConnectionException, NoSuchElementException {

        try {
            User user = userRepo.getUserByLogin(userName);
            if (security.isPasswordVerify(password, user.getPassword())) return user;
            else throw new NoSuchElementException();
        } catch (SQLException e) {
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
            return userRepo.getUserByLogin(userName);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException(e);
        } catch (NoSuchElementException e) {
            logger.error(e.getMessage());
            throw new NoSuchElementException(e);
        }
    }

    @Override
    public void blockUser(int userId) throws DbConnectionException {
        try {
            User user = userRepo.getUserById(userId);
            List<UserTariff> userTariffList = userTariffRepo.getSubscribedUserTariffList(userId);
            for (UserTariff userTariff : userTariffList) {
                if (userTariff.getSubscribeStatus().equals(SubscribeStatus.ACTIVE)) {
                    BigDecimal returnValue = userTariff.calcMoneyBackValue();
                    if (returnValue.compareTo(new BigDecimal(0)) > 0) {
                        Payment moneyBackPayment = new Payment(0, user, returnValue, new Date(), PaymentType.IN, IncomingPaymentType.MONEYBACK.getName());
                        paymentRepo.addPayment(moneyBackPayment, new ArrayList<>());
                    }
                }
                userTariff.setSubscribeStatus(SubscribeStatus.BLOCKED);
                userTariffRepo.updateUserTariff(userTariff);
            }
            userRepo.setUserStatus(user, UserStatus.BLOCKED);

            logger.info(String.format("User %s blocked ", userId));

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException(e);
        } catch (NotEnoughBalanceException ignored) {

        }
    }

    @Override
    public void unblockUser(int userId) throws DbConnectionException {
        try {
            User oldUser = userRepo.getUserById(userId);
            List<UserTariff> userTariffList = userTariffRepo.getBlockedUserTariffList(userId);
            for (UserTariff userTariff : userTariffList) {
                LocalDate date = userTariff.getTariff().getPeriod().getNexDate(LocalDate.now());
                String userTariffWithdrawDescription = String.format("%s tariff %s subscribed to %s", userTariff.getTariff().getService().getName(),
                        userTariff.getTariff().getName(), date);
                User user = userRepo.getUserById(userId);
                Payment withdraw = new Payment(0, user, userTariff.getTariff().getPrice(), new Date(), PaymentType.OUT, userTariffWithdrawDescription);
                try {
                    paymentRepo.addWithdraw(withdraw, userTariff);
                    userTariff.setSubscribeStatus(SubscribeStatus.ACTIVE);
                    userTariff.setDateEnd(date);
                    userTariffRepo.updateUserTariff(userTariff);

                } catch (NotEnoughBalanceException e) {
                    userTariff.setSubscribeStatus(SubscribeStatus.PAUSED);
                    userTariffRepo.updateUserTariff(userTariff);
                }
            }
            userRepo.setUserStatus(oldUser, UserStatus.ACTIVE);
            logger.info(String.format("User %s unblocked ", userId));

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException(e);
        }
    }

    @Override
    public void setUserPassword(int userId, String password, String confirm) throws DbConnectionException, IncorrectFormatException {
        validator.validateString(password, Regex.PASSWORD_REGEX, "alert.incorrectPassword");
        validator.validateConfirmPassword(password, confirm, "alert.passwordNotMatch");


        try {
            User user = userRepo.getUserById(userId);
            String hashPassword = security.getPasswordHash(password);
            userRepo.setUserPassword(user, hashPassword);
            logger.info(String.format("User %s password changed ", user));
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException(e);
        }
    }

    @Override
    public List<User> getUsersList(DtoTable dtoTable) throws DbConnectionException {
        List<User> users;

        try {
            Map<String, String> parameters = dtoTable.buildQueryParameters();
            users = userRepo.getUsersList(parameters);

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException(e);
        }
        return users;
    }

    @Override
    public Integer getUsersCount(DtoTable dtoTable) throws DbConnectionException {
        try {
            Map<String, String> parameters = dtoTable.buildQueryParameters();
            return userRepo.getUsersCount(parameters);

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException(e);
        }
    }

    @Override
    public Integer getTotalUsersCount() throws DbConnectionException {
        try {
            return userRepo.getUsersCount(null);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException(e);
        }
    }

    @Override
    public boolean isUserExist(String userName) throws DbConnectionException, NoSuchElementException {

        try {
            userRepo.getUserByLogin(userName);
            return true;
        } catch (SQLException e) {
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
        try {
            User user = MapperService.toUser(dtoUser);
            String hashPassword = security.getPasswordHash(dtoUser.getPassword());
            user.setPassword(hashPassword);
            User newUser = userRepo.addUser(user);
            emailService.sendEmail(user.getEmail(), "ISP Registration", "Your password: " + dtoUser.getPassword());
            logger.info(String.format("User %s created ", dtoUser.getEmail()));
            return newUser;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException(e);
        }

    }

    @Override
    public User updateUser(DtoUser dtoUser) throws DbConnectionException, IncorrectFormatException {

        validator.validateString(dtoUser.getEmail(), Regex.EMAIL_REGEX, "alert.incorrectEmailFormat");
        validator.validateString(dtoUser.getName(), Regex.NAME_REGEX, "alert.incorrectNameFormat");
        validator.validateString(dtoUser.getLastName(), Regex.NAME_REGEX, "alert.incorrectLastNameFormat");
        validator.validateString(dtoUser.getPhone(), Regex.PHONE_NUMBER_REGEX, "alert.incorrectPhoneFormat");


        try {
            User user = MapperService.toUser(dtoUser);
            userRepo.updateUserProfile(user);
            logger.info(String.format("User %s updated ", dtoUser.getEmail()));
            return user;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException(e);
        }

    }

}

