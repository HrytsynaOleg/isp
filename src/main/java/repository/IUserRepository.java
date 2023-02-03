package repository;

import dto.DtoUser;
import entity.User;
import exceptions.DbConnectionException;

import java.util.List;
import java.util.Map;

public interface IUserRepository {
    int addUser(User user) throws DbConnectionException;
    User getUserByLogin(String login) throws DbConnectionException;
    User getUserById(int userId) throws DbConnectionException;
    void updateUserProfile(DtoUser dtoUser) throws DbConnectionException;
    List<User> getUsersList(Map<String,String> parameters) throws DbConnectionException;
    Integer getUsersCount(Map<String,String> parameters) throws DbConnectionException;
    void setUserStatus(int user, String status) throws DbConnectionException;
    void setUserPassword(int user, String password) throws DbConnectionException;
}
