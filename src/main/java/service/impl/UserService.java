package service.impl;

import dao.IUserDao;
import dao.impl.UserDaoImpl;
import dto.DtoUser;
import entity.User;
import entity.builder.UserBuilder;
import enums.UserRole;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;
import service.ISecurityService;
import service.IUserService;
import service.IValidatorService;
import settings.Regex;

import java.util.NoSuchElementException;

public class UserService implements IUserService {

    private static final IUserDao userDao = new UserDaoImpl();
    private static final ISecurityService security = new SecurityService();
    private static final IValidatorService validator =  new ValidatorService();

    public User getUser(String userName, String password) throws DbConnectionException, NoSuchElementException {

        try {
            User user = userDao.getUserByLogin(userName);
            if (security.isPasswordVerify(password,user.getPassword())) return user;
            else throw new NoSuchElementException();
        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e);
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
        validator.validateString(dtoUser.getEmail(), Regex.EMAIL_REGEX,"Incorrect Email format");
        validator.validateString(dtoUser.getPassword(), Regex.PASSWORD_REGEX,"Incorrect password format");
        validator.validateConfirmPassword(dtoUser.getPassword(), dtoUser.getConfirmPassword(), "Password doesn't match");
        validator.validateString(dtoUser.getName(), Regex.NAME_REGEX,"Incorrect name format");
        validator.validateString(dtoUser.getLastName(), Regex.NAME_REGEX,"Incorrect last name format");
        validator.validateString(dtoUser.getPhone(), Regex.PHONE_NUMBER_REGEX,"Incorrect phone number format");

        User user = mapDtoToUser(dtoUser);
        try {
            int userId = userDao.addUser(user);
            user.setId(userId);
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

