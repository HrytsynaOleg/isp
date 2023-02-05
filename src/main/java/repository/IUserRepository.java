package repository;

import dto.DtoUser;
import entity.User;
import enums.UserStatus;
import exceptions.DbConnectionException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public interface IUserRepository {
    User addUser(User user) throws SQLException;
    User getUserByLogin(String login) throws NoSuchElementException, SQLException;
    User getUserById(int userId) throws NoSuchElementException, SQLException;
    void updateUserProfile(User user) throws SQLException;
    List<User> getUsersList(Map<String,String> parameters) throws SQLException;
    Integer getUsersCount(Map<String,String> parameters) throws SQLException;
    void setUserStatus(User user, UserStatus status) throws SQLException;
    void setUserPassword(User user, String password) throws SQLException;
}
