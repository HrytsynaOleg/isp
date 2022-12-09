package service.impl;

import dao.IUserDao;
import dao.impl.UserDaoImpl;
import entity.User;
import exeptions.DbConnectionExeption;
import service.IUserService;

import java.util.NoSuchElementException;

public class UserService implements IUserService {

    private static final IUserDao userDao = new UserDaoImpl();

    public User validateUser(String userName) throws DbConnectionExeption {

        try {
            return userDao.getUserByLogin(userName);
        } catch (DbConnectionExeption e) {
            throw new DbConnectionExeption(e);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e);
        }
    }
}

