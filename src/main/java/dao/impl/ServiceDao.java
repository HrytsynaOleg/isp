package dao.impl;

import dao.IDao;
import entity.Service;
import repository.QueryBuilder;
import settings.Queries;

import java.sql.*;
import java.util.*;

public class ServiceDao implements IDao<Service> {


    private Service getServiceFromResultSet(ResultSet resultSet) throws SQLException {

        return new Service(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
    }

    @Override
    public Optional<Service> get(Connection connection, long id) throws SQLException {

        PreparedStatement statement = connection.prepareStatement(Queries.GET_SERVICE_BY_ID);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return Optional.of(getServiceFromResultSet(resultSet));
        }
        return Optional.empty();
    }

    @Override
    public List<Service> getList(Connection connection, Map<String, String> parameters) throws SQLException {

        QueryBuilder queryBuilder = new QueryBuilder(Queries.GET_SERVICES_LIST, parameters);
        List<Service> list = new ArrayList<>();

        PreparedStatement statement = connection.prepareStatement(queryBuilder.build());
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Service service = getServiceFromResultSet(resultSet);
            list.add(service);
        }

        return list;
    }

    @Override
    public int getCount(Connection connection, Map<String, String> parameters) throws SQLException {

        QueryBuilder queryBuilder = new QueryBuilder(Queries.GET_SERVICES_COUNT, parameters);

        PreparedStatement statement = connection.prepareStatement(queryBuilder.buildOnlySearch());
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }

        return 0;
    }

    @Override
    public Optional<Service> add(Connection connection, Service service) throws SQLException {

            PreparedStatement statement = connection.prepareStatement(Queries.INSERT_SERVICE, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, service.getName());
            statement.setString(2, service.getDescription());

            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                service.setId(keys.getInt(1));
                return Optional.of(service);
            }
            return Optional.empty();

    }

    @Override
    public void update(Connection connection, Service service) throws SQLException {

        PreparedStatement statement = connection.prepareStatement(Queries.UPDATE_SERVICE_BY_ID);
        statement.setString(1, service.getName());
        statement.setString(2, service.getDescription());
        statement.setInt(3, service.getId());
        statement.executeUpdate();
    }

    @Override
    public void delete(Connection connection, long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Queries.DELETE_SERVICE_BY_ID);
        statement.setLong(1, id);
        statement.executeUpdate();
    }
}
