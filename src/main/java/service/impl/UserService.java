package service.impl;

import repository.IUserRepository;
import repository.IUserTariffRepository;
import dto.DtoTable;
import dto.DtoUser;
import entity.User;
import entity.UserTariff;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import service.*;
import settings.Regex;

import java.sql.SQLException;
import java.util.*;

public class UserService implements IUserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);


    private final IUserRepository userRepo;
    private final IUserTariffRepository userTariffRepo;
    private final SecurityService security;
    private static final IEmailService emailService = new EmailService();

    public UserService(IUserRepository userRepo, IUserTariffRepository userTariffRepo, SecurityService security) {
        this.userRepo = userRepo;
        this.userTariffRepo = userTariffRepo;
        this.security = security;
    }

    public User getUser(String userName, String password) throws DbConnectionException, NoSuchElementException {

        try {
            User user = userRepo.getUserByLogin(userName);
            if (security.isPasswordVerify(password, user.getPassword())) return user;
            else throw new NoSuchElementException();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException("alert.databaseError");
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("alert.notFoundUser");
        }
    }

    @Override
    public User getUserByLogin(String userName) throws DbConnectionException {
        try {
            return userRepo.getUserByLogin(userName);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException("alert.databaseError");
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("alert.notFoundUser");
        }
    }

    @Override
    public void blockUser(int userId) throws DbConnectionException {

        try {
            User user = userRepo.getUserById(userId);
            List<UserTariff> userTariffList = userTariffRepo.getSubscribedUserTariffList(userId);
            userRepo.blockUser(user, userTariffList);
            logger.info(String.format("User %s blocked ", userId));

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public void unblockUser(int userId) throws DbConnectionException {
        try {
            User user = userRepo.getUserById(userId);
            List<UserTariff> userTariffList = userTariffRepo.getBlockedUserTariffList(userId);
            userRepo.unblockUser(user, userTariffList);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public void setUserPassword(int userId, String password, String confirm) throws DbConnectionException, IncorrectFormatException {

        ValidatorService.validateString(password, Regex.PASSWORD_REGEX, "alert.incorrectPassword");
        ValidatorService.validateConfirmPassword(password, confirm, "alert.passwordNotMatch");

        try {
            User user = userRepo.getUserById(userId);
            String hashPassword = security.getPasswordHash(password);
            userRepo.setUserPassword(user, hashPassword);
            logger.info(String.format("User %s password changed ", user));
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException("alert.databaseError");
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
            throw new DbConnectionException("alert.databaseError");
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
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public Integer getTotalUsersCount() throws DbConnectionException {
        try {
            return userRepo.getUsersCount(null);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public boolean isUserExist(String userName) throws DbConnectionException {

        try {
            userRepo.getUserByLogin(userName);
            return true;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException("alert.databaseError");
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Override
    public User addUser(DtoUser dtoUser) throws IncorrectFormatException, DbConnectionException {

        ValidatorService.validateString(dtoUser.getEmail(), Regex.EMAIL_REGEX, "alert.incorrectEmail");
        ValidatorService.validateString(dtoUser.getPassword(), Regex.PASSWORD_REGEX, "alert.incorrectPassword");
        ValidatorService.validateString(dtoUser.getName(), Regex.NAME_REGEX, "alert.incorrectName");
        ValidatorService.validateString(dtoUser.getLastName(), Regex.NAME_REGEX, "alert.incorrectLastName");
        ValidatorService.validateString(dtoUser.getPhone(), Regex.PHONE_NUMBER_REGEX, "alert.incorrectPhone");
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
            throw new DbConnectionException("alert.databaseError");
        }
    }

    @Override
    public User updateUser(DtoUser dtoUser) throws DbConnectionException, IncorrectFormatException {

        ValidatorService.validateString(dtoUser.getEmail(), Regex.EMAIL_REGEX, "alert.incorrectEmailFormat");
        ValidatorService.validateString(dtoUser.getName(), Regex.NAME_REGEX, "alert.incorrectNameFormat");
        ValidatorService.validateString(dtoUser.getLastName(), Regex.NAME_REGEX, "alert.incorrectLastNameFormat");
        ValidatorService.validateString(dtoUser.getPhone(), Regex.PHONE_NUMBER_REGEX, "alert.incorrectPhoneFormat");

        try {
            User user = MapperService.toUser(dtoUser);
            userRepo.updateUserProfile(user);
            logger.info(String.format("User %s updated ", dtoUser.getEmail()));
            return user;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException("alert.databaseError");
        }
    }
}

