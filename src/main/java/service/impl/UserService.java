package service.impl;

import dao.IUserDao;
import dao.impl.UserDaoImpl;
import entity.User;
import exceptions.DbConnectionException;
import service.IUserService;

import java.util.NoSuchElementException;

public class UserService implements IUserService {

    private static final IUserDao userDao = new UserDaoImpl();

    public User validateUser(String userName) throws DbConnectionException {

        try {
            return userDao.getUserByLogin(userName);
        } catch (DbConnectionException e) {
            throw new DbConnectionException(e);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e);
        }
    }
}

