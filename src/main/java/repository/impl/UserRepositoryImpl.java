package repository.impl;

import connector.DbConnectionPool;
import dao.IDao;
import repository.IUserRepository;
import entity.User;
import enums.UserStatus;
import java.sql.*;
import java.util.*;

public class UserRepositoryImpl implements IUserRepository {
    private final IDao userDao;

    public UserRepositoryImpl(IDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User addUser(User user) throws SQLException {

        try (Connection connection = DbConnectionPool.getConnection()) {
            Optional<User> result = userDao.add(connection, user);
            result.orElseThrow(SQLException::new);
            return result.get();
        }
    }

    @Override
    public User getUserByLogin(String login) throws NoSuchElementException, SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("whereColumn", "user_email");
            parameters.put("whereValue", login);
            Optional<User> user = userDao.getList(connection, parameters).stream().findFirst();
            user.orElseThrow(NoSuchElementException::new);
            return user.get();
        }
    }

    @Override
    public User getUserById(int userId) throws NoSuchElementException, SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            Optional<User> user = userDao.get(connection, userId);
            user.orElseThrow(NoSuchElementException::new);
            return user.get();
        }
    }

    @Override
    public void updateUserProfile(User user) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            userDao.update(connection, user);
        }
    }

    @Override
    public List<User> getUsersList(Map<String, String> parameters) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            return userDao.getList(connection, parameters);
        }

    }

    @Override
    public Integer getUsersCount(Map<String, String> parameters) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            return userDao.getCount(connection, parameters);
        }
    }

    @Override
    public void setUserStatus(User user, UserStatus status) throws SQLException {
        try(Connection connection = DbConnectionPool.getConnection()) {
            user.setStatus(status);
            userDao.update(connection, user);
        }
    }

    @Override
    public void setUserPassword(User user, String password) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            user.setPassword(password);
            userDao.update(connection, user);
        }
    }

}
