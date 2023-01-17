package dao;

import dto.DtoUser;
import entity.User;
import exceptions.DbConnectionException;

import java.util.List;

public interface IUserDao {
    int addUser(User user) throws DbConnectionException;
    User getUserByLogin(String login) throws DbConnectionException;
    User getUserById(int userId) throws DbConnectionException;
    void updateUserProfile(DtoUser dtoUser) throws DbConnectionException;
    List<User> getUsersList(Integer limit, Integer total, Integer sort, String order) throws DbConnectionException;
    List<User> getFindUsersList(Integer limit, Integer total, Integer sort, String order, int field, String criteria) throws DbConnectionException;
    Integer getUsersCount() throws DbConnectionException;
    Integer getFindUsersCount(int field, String criteria) throws DbConnectionException;
    void setUserStatus(int user, String status) throws DbConnectionException;
    void setUserPassword(int user, String password) throws DbConnectionException;
}
