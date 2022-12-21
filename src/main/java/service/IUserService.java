package service;

import entity.User;
import exceptions.DbConnectionException;

public interface IUserService {
    User validateUser(String userName) throws DbConnectionException;
}
