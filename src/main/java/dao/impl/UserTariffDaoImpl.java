package dao.impl;

import dao.IDao;
import dao.QueryBuilder;
import dependecies.DependencyManager;
import entity.Service;
import entity.Tariff;
import entity.User;
import entity.UserTariff;
import enums.BillingPeriod;
import enums.SubscribeStatus;
import enums.TariffStatus;
import settings.Patterns;
import settings.Queries;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserTariffDaoImpl extends AbstractDao implements IDao<UserTariff> {

    @Override
    public UserTariff getItemFromResultSet(ResultSet resultSet) throws SQLException {

        User user = DependencyManager.userRepo.getUserById(resultSet.getInt(2));
        Tariff tariff = DependencyManager.tariffRepo.getTariffById(resultSet.getInt(3));
        return new UserTariff(resultSet.getInt(1), user, tariff,
                SubscribeStatus.valueOf(resultSet.getString(4)),
                resultSet.getDate(5).toLocalDate(), resultSet.getDate(6).toLocalDate());
    }

    @Override
    public UserTariff setItemId(Object item, int id) {
        UserTariff userTariff = (UserTariff) item;
        userTariff.setId(id);
        return userTariff;
    }

    @Override
    public PreparedStatement getGetStatement(Connection connection, long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Queries.GET_USER_TARIFF_BY_ID);
        statement.setLong(1, id);
        return statement;
    }

    @Override
    public PreparedStatement getListStatement(Connection connection, Map<String, String> parameters) throws SQLException {
        QueryBuilder queryBuilder = new QueryBuilder(Queries.GET_USER_TARIFFS_LIST, parameters);
        return connection.prepareStatement(queryBuilder.build());
    }

    @Override
    public PreparedStatement getCountStatement(Connection connection, Map<String, String> parameters) throws SQLException {
        QueryBuilder queryBuilder = new QueryBuilder(Queries.GET_USER_TARIFF_COUNT, parameters);
        return connection.prepareStatement(queryBuilder.buildOnlySearch());
    }

    @Override
    public PreparedStatement getAddStatement(Connection connection, Object entity) throws SQLException {
        UserTariff userTariff = (UserTariff) entity;
        PreparedStatement statement = connection.prepareStatement(Queries.INSERT_USER_TARIFF, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, userTariff.getUser().getId());
        statement.setInt(2, userTariff.getTariff().getId());
        statement.setString(3, String.valueOf(SubscribeStatus.ACTIVE));
        statement.setString(4, String.valueOf(userTariff.getDateBegin()));
        statement.setString(5, String.valueOf(userTariff.getDateEnd()));
        return statement;
    }

    @Override
    public PreparedStatement getUpdateStatement(Connection connection, Object entity) throws SQLException {
        UserTariff userTariff = (UserTariff) entity;
        PreparedStatement statement = connection.prepareStatement(Queries.UPDATE_USER_TARIFF);
        statement.setInt(1, userTariff.getUser().getId());
        statement.setInt(2, userTariff.getTariff().getId());
        statement.setString(3, String.valueOf(userTariff.getSubscribeStatus()));
        statement.setString(4, String.valueOf(userTariff.getDateBegin()));
        statement.setString(5, String.valueOf(userTariff.getDateEnd()));
        statement.setInt(6, userTariff.getId());
        return statement;
    }

    @Override
    public PreparedStatement getDeleteStatement(Connection connection, long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Queries.DELETE_USER_TARIFF);
        statement.setLong(1, id);
        return statement;
    }

    @Override
    public Optional<UserTariff> get(Connection connection, long id) throws SQLException {
        return super.getRecord(connection, id);
    }

    @Override
    public List<UserTariff> getList(Connection connection, Map<String, String> parameters) throws SQLException {
        return super.getRecordsList(connection, parameters);
    }

    @Override
    public int getCount(Connection connection, Map<String, String> parameters) throws SQLException {
        return super.getRecordsCount(connection, parameters);
    }

    @Override
    public Optional<UserTariff> add(Connection connection, UserTariff tariff) throws SQLException {
        return super.addRecord(connection, tariff);
    }

    @Override
    public void update(Connection connection, UserTariff tariff) throws SQLException {
        super.updateRecord(connection, tariff);
    }

    @Override
    public void delete(Connection connection, long id) throws SQLException {
        super.deleteRecord(connection, id);
    }
}
