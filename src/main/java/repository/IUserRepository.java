package repository;

import entity.User;
import entity.UserTariff;
import enums.UserStatus;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public interface IUserRepository {
    User addUser(User user) throws SQLException;

    User getUserByLogin(String login) throws NoSuchElementException, SQLException;

    User getUserById(int userId) throws NoSuchElementException, SQLException;

    void updateUserProfile(User user) throws SQLException;

    List<User> getUsersList(Map<String, String> parameters) throws SQLException;

    Integer getUsersCount(Map<String, String> parameters) throws SQLException;

    void setUserPassword(User user, String password) throws SQLException;

    void blockUser(User user, List<UserTariff> userTariffList) throws SQLException;

    void unblockUser(User user, List<UserTariff> userTariffList) throws SQLException;
}
