package dao.impl;

import connector.DbConnectionPool;
import dao.IUserDao;
import dao.QueryBuilder;
import dto.DtoUser;
import entity.User;
import entity.builder.UserBuilder;
import enums.UserRole;
import enums.UserStatus;
import exceptions.DbConnectionException;
import settings.Queries;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class UserDaoImpl implements IUserDao {
    @Override
    public int addUser(User user) throws DbConnectionException {

        try (Connection connection = DbConnectionPool.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(Queries.INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().toString());
            statement.setString(4, user.getStatus().toString());
            statement.setString(5, user.getName());
            statement.setString(6, user.getLastName());
            statement.setString(7, user.getPhone());
            statement.setString(8, user.getAdress());
            statement.setString(9, String.valueOf(user.getBalance()));

            String pattern = "yyyy-MM-dd H:m:s.S";
            String dateInString =new SimpleDateFormat(pattern).format(user.getRegistration());

            statement.setString(10, dateInString);
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            user.setId(keys.getInt(1));
            return keys.getInt(1);

        } catch (SQLException | NoClassDefFoundError |ExceptionInInitializerError e) {
            throw new DbConnectionException("Add user database error", e);
        }
    }

    @Override
    public User getUserByLogin(String login) throws DbConnectionException, NoSuchElementException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.GET_USER_BY_LOGIN);
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getUserFromResultSet(resultSet);
            }
        } catch (SQLException | NoClassDefFoundError |ExceptionInInitializerError e) {
            throw new DbConnectionException("Find user database error", e);
        }
        throw new NoSuchElementException("User not found");
    }

    @Override
    public User getUserById(int userId) throws DbConnectionException, NoSuchElementException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.GET_USER_BY_ID);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getUserFromResultSet(resultSet);
            }
        } catch (SQLException | NoClassDefFoundError |ExceptionInInitializerError e) {
            throw new DbConnectionException("Find user database error", e);
        }
        throw new NoSuchElementException("User not found");
    }

    @Override
    public void updateUserProfile(DtoUser dtoUser) throws DbConnectionException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.UPDATE_USER_PROFILE_BY_ID);
            statement.setString(1, dtoUser.getEmail());
            statement.setString(2, dtoUser.getName());
            statement.setString(3, dtoUser.getLastName());
            statement.setString(4, dtoUser.getPhone());
            statement.setString(5, dtoUser.getAddress());
            statement.setString(6, dtoUser.getId());
            statement.executeUpdate();

        } catch (SQLException | NoClassDefFoundError |ExceptionInInitializerError e) {
            throw new DbConnectionException("Update user database error", e);
        }
    }

    @Override
    public List<User> getUsersList(Map<String,String> parameters) throws DbConnectionException {
        QueryBuilder queryBuilder = new QueryBuilder(Queries.GET_USERS_LIST, parameters);
        List<User> list = new ArrayList<>();
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryBuilder.build());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = getUserFromResultSet(resultSet);
                list.add(user);
            }
        } catch (SQLException | NoClassDefFoundError |ExceptionInInitializerError e) {
            throw new DbConnectionException("List user database error", e);
        }

        return list;
    }


    @Override
    public Integer getUsersCount(Map<String,String> parameters) throws DbConnectionException {
        QueryBuilder queryBuilder = new QueryBuilder(Queries.GET_USERS_COUNT, parameters);
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryBuilder.buildOnlySearch());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException | NoClassDefFoundError |ExceptionInInitializerError e) {
            throw new DbConnectionException("List user database error", e);
        }
        return null;
    }


    @Override
    public void setUserStatus(int user, String status) throws DbConnectionException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.UPDATE_USER_STATUS);
            statement.setString(1, status);
            statement.setInt(2, user);
            statement.executeUpdate();
        } catch (SQLException | NoClassDefFoundError |ExceptionInInitializerError e) {
            throw new DbConnectionException("Set user status database error", e);
        }
    }

    @Override
    public void setUserPassword(int user, String password) throws DbConnectionException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.UPDATE_USER_PASSWORD);
            statement.setString(1, password);
            statement.setInt(2, user);
            statement.executeUpdate();
        } catch (SQLException | NoClassDefFoundError |ExceptionInInitializerError e) {
            throw new DbConnectionException("Set user password database error", e);
        }
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        UserRole userRole = UserRole.valueOf(resultSet.getString(4));
        UserStatus userStatus = UserStatus.valueOf(resultSet.getString(5));
        return new UserBuilder()
                .setUserId(resultSet.getInt(1))
                .setUserEmail(resultSet.getString(2))
                .setUserPassword(resultSet.getString(3))
                .setUserRole(userRole)
                .setUserStatus(userStatus)
                .setUserName(resultSet.getString(6))
                .setUserLastName(resultSet.getString(7))
                .setUserPhone(resultSet.getString(8))
                .setUserAdress(resultSet.getString(9))
                .setUserBalance(resultSet.getString(10))
                .setUserRegistration(resultSet.getDate(11))
                .build();
    }

}
