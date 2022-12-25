package service;

import entity.User;
import exceptions.DbConnectionException;

import java.util.NoSuchElementException;

public interface IUserService {
    User getUser(String userName, String password) throws DbConnectionException;
    boolean isUserExist(String userName) throws DbConnectionException, NoSuchElementException;
}
