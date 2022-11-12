package dao;

import entity.User;

public interface IUserDao {
    void addUser();
    User getUserByLogin(String login);
}
