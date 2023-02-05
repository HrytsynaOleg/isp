package dao.impl;

import dao.IDao;
import entity.Service;
import dao.QueryBuilder;
import settings.Queries;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ServiceDaoImpl extends AbstractDao implements IDao<Service> {

    @Override
    public Service getItemFromResultSet(ResultSet resultSet) throws SQLException {
        return new Service(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
    }

    @Override
    public Service setItemId(Object item, int id) {
        Service service = (Service) item;
        service.setId(id);
        return service;
    }

    @Override
    public PreparedStatement getGetStatement(Connection connection, long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Queries.GET_SERVICE_BY_ID);
        statement.setLong(1, id);
        return statement;
    }

    @Override
    public PreparedStatement getListStatement(Connection connection, Map<String, String> parameters) throws SQLException {
        QueryBuilder queryBuilder = new QueryBuilder(Queries.GET_SERVICES_LIST, parameters);
        return connection.prepareStatement(queryBuilder.build());
    }

    @Override
    public PreparedStatement getCountStatement(Connection connection, Map<String, String> parameters) throws SQLException {
        QueryBuilder queryBuilder = new QueryBuilder(Queries.GET_SERVICES_COUNT, parameters);
        return connection.prepareStatement(queryBuilder.buildOnlySearch());
    }

    @Override
    public PreparedStatement getAddStatement(Connection connection, Object entity) throws SQLException {
        Service service = (Service) entity;
        PreparedStatement statement = connection.prepareStatement(Queries.INSERT_SERVICE, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, service.getName());
        statement.setString(2, service.getDescription());
        return statement;
    }

    @Override
    public PreparedStatement getUpdateStatement(Connection connection, Object entity) throws SQLException {
        Service service = (Service) entity;
        PreparedStatement statement = connection.prepareStatement(Queries.UPDATE_SERVICE_BY_ID);
        statement.setString(1, service.getName());
        statement.setString(2, service.getDescription());
        statement.setInt(3, service.getId());
        return statement;
    }

    @Override
    public PreparedStatement getDeleteStatement(Connection connection, long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Queries.DELETE_SERVICE_BY_ID);
        statement.setLong(1, id);
        return statement;
    }

    @Override
    public Optional<Service> get(Connection connection, long id) throws SQLException {
        return super.getRecord(connection, id);
    }

    @Override
    public List<Service> getList(Connection connection, Map<String, String> parameters) throws SQLException {
        return super.getRecordsList(connection, parameters);
    }

    @Override
    public int getCount(Connection connection, Map<String, String> parameters) throws SQLException {
        return super.getRecordsCount(connection, parameters);
    }

    @Override
    public Optional<Service> add(Connection connection, Service service) throws SQLException {
        return super.addRecord(connection, service);
    }

    @Override
    public void update(Connection connection, Service service) throws SQLException {
        super.updateRecord(connection, service);
    }

    @Override
    public void delete(Connection connection, long id) throws SQLException {
        super.deleteRecord(connection, id);
    }
}
