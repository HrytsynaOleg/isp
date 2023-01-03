package dao;

import dto.DtoUser;
import entity.User;
import exceptions.DbConnectionException;

public interface IUserDao {
    int addUser(User user) throws DbConnectionException;
    User getUserByLogin(String login) throws DbConnectionException;
    void updateUserProfile(DtoUser dtoUser) throws DbConnectionException;
}
