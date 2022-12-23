package service;

import entity.User;
import exceptions.DbConnectionException;

public interface IUserService {
    User getUser(String userName, String password) throws DbConnectionException;
}
