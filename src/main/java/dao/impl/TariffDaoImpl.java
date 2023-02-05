package dao.impl;

import dao.IDao;
import dao.QueryBuilder;
import dependecies.DependencyManager;
import entity.Service;
import entity.Tariff;
import enums.BillingPeriod;
import enums.TariffStatus;
import settings.Queries;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TariffDaoImpl extends AbstractDao implements IDao<Tariff> {

    @Override
    public Tariff getItemFromResultSet(ResultSet resultSet) throws SQLException {

        Service service = DependencyManager.serviceRepo.getServiceById(resultSet.getInt(2));

        return new Tariff(resultSet.getInt(1), service, resultSet.getString(3),
                resultSet.getString(5), BigDecimal.valueOf(resultSet.getDouble(4)),
                BillingPeriod.valueOf(resultSet.getString(6)), TariffStatus.valueOf(resultSet.getString(7)));
    }


    @Override
    public Tariff setItemId(Object item, int id) {
        Tariff tariff = (Tariff) item;
        tariff.setId(id);
        return tariff;
    }

    @Override
    public PreparedStatement getGetStatement(Connection connection, long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Queries.GET_TARIFF_BY_ID);
        statement.setLong(1, id);
        return statement;
    }

    @Override
    public PreparedStatement getListStatement(Connection connection, Map<String, String> parameters) throws SQLException {
        QueryBuilder queryBuilder = new QueryBuilder(Queries.GET_TARIFFS_LIST, parameters);
        return connection.prepareStatement(queryBuilder.build());
    }

    @Override
    public PreparedStatement getCountStatement(Connection connection, Map<String, String> parameters) throws SQLException {
        QueryBuilder queryBuilder = new QueryBuilder(Queries.GET_TARIFFS_COUNT, parameters);
        return connection.prepareStatement(queryBuilder.buildOnlySearch());
    }

    @Override
    public PreparedStatement getAddStatement(Connection connection, Object entity) throws SQLException {
        Tariff tariff = (Tariff) entity;
        PreparedStatement statement = connection.prepareStatement(Queries.INSERT_TARIFF, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, tariff.getService().getId());
        statement.setString(2, tariff.getName());
        statement.setString(3, String.valueOf(tariff.getPrice()));
        statement.setString(4, tariff.getDescription());
        statement.setString(5, String.valueOf(tariff.getPeriod()));
        statement.setString(6, String.valueOf(tariff.getStatus()));
        return statement;
    }

    @Override
    public PreparedStatement getUpdateStatement(Connection connection, Object entity) throws SQLException {
        Tariff tariff = (Tariff) entity;
        PreparedStatement statement = connection.prepareStatement(Queries.UPDATE_TARIFF_BY_ID);
        statement.setString(1, tariff.getName());
        statement.setString(2, String.valueOf(tariff.getPrice()));
        statement.setString(3, tariff.getDescription());
        statement.setString(4, tariff.getPeriod().toString());
        statement.setString(5, tariff.getStatus().toString());
        statement.setInt(6, tariff.getId());
        return statement;
    }

    @Override
    public PreparedStatement getDeleteStatement(Connection connection, long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Queries.DELETE_TARIFF_BY_ID);
        statement.setLong(1, id);
        return statement;
    }

    @Override
    public Optional<Tariff> get(Connection connection, long id) throws SQLException {
        return super.getRecord(connection, id);
    }

    @Override
    public List<Tariff> getList(Connection connection, Map<String, String> parameters) throws SQLException {
        return super.getRecordsList(connection, parameters);
    }

    @Override
    public int getCount(Connection connection, Map<String, String> parameters) throws SQLException {
        return super.getRecordsCount(connection, parameters);
    }

    @Override
    public Optional<Tariff> add(Connection connection, Tariff tariff) throws SQLException {
        return super.addRecord(connection, tariff);
    }

    @Override
    public void update(Connection connection, Tariff tariff) throws SQLException {
        super.updateRecord(connection, tariff);
    }

    @Override
    public void delete(Connection connection, long id) throws SQLException {
        super.deleteRecord(connection, id);
    }
}
