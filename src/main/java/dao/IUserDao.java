package dao;

import entity.User;
import exeptions.DbConnectionExeption;

public interface IUserDao {
    int addUser(User user) throws DbConnectionExeption;
    User getUserByLogin(String login) throws DbConnectionExeption;
}
