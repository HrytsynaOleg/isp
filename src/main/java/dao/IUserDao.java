package dao;

import dto.DtoUser;
import entity.User;
import exceptions.DbConnectionException;

import java.util.List;

public interface IUserDao {
    int addUser(User user) throws DbConnectionException;
    User getUserByLogin(String login) throws DbConnectionException;
    void updateUserProfile(DtoUser dtoUser) throws DbConnectionException;
    List<User> getUsersList(Integer limit, Integer total, Integer sort, String order) throws DbConnectionException;
    Integer getUsersCount() throws DbConnectionException;
}
