package service.impl;

import dao.IUserDao;
import dao.impl.UserDaoImpl;
import dto.DtoUser;
import entity.User;
import entity.builder.UserBuilder;
import enums.SortOrder;
import enums.UserRole;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import service.ISecurityService;
import service.IUserService;
import service.IValidatorService;
import settings.Regex;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

public class UserService implements IUserService {

    private static final IUserDao userDao = new UserDaoImpl();
    private static final ISecurityService security = new SecurityService();
    private static final IValidatorService validator = new ValidatorService();

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

    public List<User> getUsersList(Integer limit, Integer total, Integer sortColumn, SortOrder sortOrder) throws DbConnectionException {
        List<User> users;

        try {
            users = userDao.getUsersList(limit, total, sortColumn, sortOrder.toString());

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
        return users;
    }

    public List<User> getFindUsersList(Integer limit, Integer total, Integer sortColumn, SortOrder sortOrder, int field, String criteria) throws DbConnectionException {
        List<User> users;

        try {
            users = userDao.getFindUsersList(limit, total, sortColumn, sortOrder.toString(), field, criteria);

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
        return users;
    }

    public Integer getUsersCount() throws DbConnectionException {
        try {
            return userDao.getUsersCount();

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
    }

    public Integer getFindUsersCount(int field, String criteria) throws DbConnectionException {
        try {
            return userDao.getFindUsersCount(field, criteria);

        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }
    }

    public boolean isUserExist(String userName) throws DbConnectionException, NoSuchElementException {

        try {
            User user = userDao.getUserByLogin(userName);
            return true;
        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public User addUser(DtoUser dtoUser) throws IncorrectFormatException, DbConnectionException {

        validator.validateString(dtoUser.getEmail(), Regex.EMAIL_REGEX, "Incorrect Email format");
        validator.validateString(dtoUser.getPassword(), Regex.PASSWORD_REGEX, "Incorrect password format");
//        validator.validateConfirmPassword(dtoUser.getPassword(), dtoUser.getConfirmPassword(), "Password doesn't match");
        validator.validateString(dtoUser.getName(), Regex.NAME_REGEX, "Incorrect name format");
        validator.validateString(dtoUser.getLastName(), Regex.NAME_REGEX, "Incorrect last name format");
        validator.validateString(dtoUser.getPhone(), Regex.PHONE_NUMBER_REGEX, "Incorrect phone number format");

        User user = mapDtoToUser(dtoUser);
        user.setRegistration(new Date());
        user.setBalance(BigDecimal.valueOf(0));
        try {
            int userId = userDao.addUser(user);
            user.setId(userId);
        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        }

        return user;
    }

    public User updateUser(DtoUser dtoUser) throws DbConnectionException, IncorrectFormatException {

        validator.validateString(dtoUser.getEmail(), Regex.EMAIL_REGEX, "alert.incorrectEmailFormat");
        validator.validateString(dtoUser.getName(), Regex.NAME_REGEX, "alert.incorrectNameFormat");
        validator.validateString(dtoUser.getLastName(), Regex.NAME_REGEX, "alert.incorrectLastNameFormat");
        validator.validateString(dtoUser.getPhone(), Regex.PHONE_NUMBER_REGEX, "alert.incorrectPhoneFormat");

        User user;
        try {
            userDao.updateUserProfile(dtoUser);
            user = userDao.getUserByLogin(dtoUser.getEmail());
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

