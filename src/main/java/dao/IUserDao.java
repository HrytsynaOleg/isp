package dao;

import entity.User;
import exceptions.DbConnectionException;

public interface IUserDao {
    int addUser(User user) throws DbConnectionException;
    User getUserByLogin(String login) throws DbConnectionException;
}
