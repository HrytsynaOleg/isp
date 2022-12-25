package service.impl;

import dao.IUserDao;
import dao.impl.UserDaoImpl;
import entity.User;
import exceptions.DbConnectionException;
import service.ISecurityService;
import service.IUserService;

import java.util.NoSuchElementException;

public class UserService implements IUserService {

    private static final IUserDao userDao = new UserDaoImpl();
    private static final ISecurityService security = new SecurityService();

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
}

