package dao.impl;

import connector.DbConnectionPool;
import dao.IServiceDao;
import dao.IUserDao;
import dao.QueryBuilder;
import dto.DtoService;
import dto.DtoUser;
import entity.Service;
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

public class ServiceDaoImpl implements IServiceDao {
    @Override
    public int addService(Service service) throws SQLException {

        try (Connection connection = DbConnectionPool.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(Queries.INSERT_SERVICE, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, service.getName());
            statement.setString(2, service.getDescription());

            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            return keys.getInt(1);
        }
    }

    @Override
    public Service getServiceByName(String name) throws NoSuchElementException, SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.GET_SERVICE_BY_NAME);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getServiceFromResultSet(resultSet);
            }
        }
        throw new NoSuchElementException("Service not found");
    }

    @Override
    public Service getServiceById(int id) throws NoSuchElementException, SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.GET_SERVICE_BY_ID);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getServiceFromResultSet(resultSet);
            }
        }
        throw new NoSuchElementException("Service not found");
    }

    @Override
    public boolean isServiceNameExist(String name) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.GET_SERVICE_BY_NAME);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateService(DtoService dtoService) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.UPDATE_SERVICE_BY_ID);
            statement.setString(1, dtoService.getName());
            statement.setString(2, dtoService.getDescription());
            statement.setInt(3, Integer.parseInt(dtoService.getId()));
            statement.executeUpdate();

        }
    }

    @Override
    public void deleteService(int id) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.DELETE_SERVICE_BY_ID);
            statement.setInt(1, id);
            statement.executeUpdate();

        }
    }

    @Override
    public List<Service> getServicesList(Map<String,String> parameters) throws SQLException {
        QueryBuilder queryBuilder = new QueryBuilder(Queries.GET_SERVICES_LIST, parameters);
        List<Service> list = new ArrayList<>();
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryBuilder.build());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Service service = getServiceFromResultSet(resultSet);
                list.add(service);
            }
        }

        return list;
    }

    @Override
    public Integer getServicesCount(Map<String, String> parameters) throws SQLException {
        QueryBuilder queryBuilder = new QueryBuilder(Queries.GET_SERVICES_COUNT, parameters);
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryBuilder.buildOnlySearch());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return null;
    }



    private Service getServiceFromResultSet(ResultSet resultSet) throws SQLException {

        return  new Service(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3));
    }

}
