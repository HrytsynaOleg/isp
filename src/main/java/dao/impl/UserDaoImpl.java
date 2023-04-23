package dao.impl;

import dao.IDao;
import dao.QueryBuilder;
import entity.User;
import entity.builder.UserBuilder;
import enums.UserRole;
import enums.UserStatus;
import settings.Patterns;
import settings.Queries;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserDaoImpl extends AbstractDao implements IDao<User> {

    @Override
    public User getItemFromResultSet(ResultSet resultSet) throws SQLException {
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

    @Override
    public User setItemId(Object item, int id) {
        User service = (User) item;
        service.setId(id);
        return service;
    }

    @Override
    public PreparedStatement getGetStatement(Connection connection, long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Queries.GET_USER_BY_ID);
        statement.setLong(1, id);
        return statement;
    }

    @Override
    public PreparedStatement getListStatement(Connection connection, Map<String, String> parameters) throws SQLException {
        QueryBuilder queryBuilder = new QueryBuilder(Queries.GET_USERS_LIST, parameters);
        return connection.prepareStatement(queryBuilder.build());
    }

    @Override
    public PreparedStatement getCountStatement(Connection connection, Map<String, String> parameters) throws SQLException {
        QueryBuilder queryBuilder = new QueryBuilder(Queries.GET_USERS_COUNT, parameters);
        return connection.prepareStatement(queryBuilder.buildOnlySearch());
    }

    @Override
    public PreparedStatement getAddStatement(Connection connection, Object entity) throws SQLException {
        User user = (User) entity;
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
        String pattern = Patterns.datePattern;
        String dateInString = new SimpleDateFormat(pattern).format(user.getRegistration());
        statement.setString(10, dateInString);
        return statement;
    }

    @Override
    public PreparedStatement getUpdateStatement(Connection connection, Object entity) throws SQLException {
        User user = (User) entity;
        PreparedStatement statement = connection.prepareStatement(Queries.UPDATE_USER_BY_ID);
        statement.setString(1, user.getEmail());
        statement.setString(2, user.getName());
        statement.setString(3, user.getLastName());
        statement.setString(4, user.getPhone());
        statement.setString(5, user.getAdress());
        statement.setString(6, String.valueOf(user.getBalance()));
        statement.setString(7, user.getStatus().toString());

        statement.setInt(8, user.getId());
        return statement;
    }

    @Override
    public PreparedStatement getDeleteStatement(Connection connection, long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Queries.DELETE_USER_BY_ID);
        statement.setLong(1, id);
        return statement;
    }

    @Override
    public Optional<User> get(Connection connection, long id) throws SQLException {
        return super.getRecord(connection, id);
    }

    @Override
    public List<User> getList(Connection connection, Map<String, String> parameters) throws SQLException {
        return super.getRecordsList(connection, parameters);
    }

    @Override
    public int getCount(Connection connection, Map<String, String> parameters) throws SQLException {
        return super.getRecordsCount(connection, parameters);
    }

    @Override
    public Optional<User> add(Connection connection, User user) throws SQLException {
        return super.addRecord(connection, user);
    }

    @Override
    public void update(Connection connection, User user) throws SQLException {
        super.updateRecord(connection, user);
    }

    @Override
    public void delete(Connection connection, long id) throws SQLException {
        super.deleteRecord(connection, id);
    }
}
