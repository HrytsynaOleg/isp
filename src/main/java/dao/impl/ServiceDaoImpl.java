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
    public int addService(Service service) throws DbConnectionException {

        try (Connection connection = DbConnectionPool.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(Queries.INSERT_SERVICE, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, service.getName());
            statement.setString(2, service.getDescription());

            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            return keys.getInt(1);

        } catch (SQLException e) {
            throw new DbConnectionException("Add service database error", e);
        }
    }

    @Override
    public Service getServiceByName(String name) throws DbConnectionException, NoSuchElementException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.GET_SERVICE_BY_NAME);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getServiceFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DbConnectionException("Find service database error", e);
        }
        throw new NoSuchElementException("Service not found");
    }

    @Override
    public Service getServiceById(int id) throws DbConnectionException, NoSuchElementException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.GET_SERVICE_BY_ID);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getServiceFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DbConnectionException("Find service database error", e);
        }
        throw new NoSuchElementException("Service not found");
    }

    @Override
    public void updateService(DtoService dtoService) throws DbConnectionException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.UPDATE_SERVICE_BY_ID);
            statement.setString(1, dtoService.getName());
            statement.setString(2, dtoService.getDescription());
            statement.setInt(3, Integer.parseInt(dtoService.getId()));
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DbConnectionException("Update service database error", e);
        }
    }

    @Override
    public void deleteService(int id) throws DbConnectionException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.DELETE_SERVICE_BY_ID);
            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DbConnectionException("Delete service database error", e);
        }
    }

    @Override
    public List<Service> getServicesList(Map<String,String> parameters) throws DbConnectionException {
        QueryBuilder queryBuilder = new QueryBuilder(Queries.GET_SERVICES_LIST, parameters);
        List<Service> list = new ArrayList<>();
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryBuilder.build());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Service service = getServiceFromResultSet(resultSet);
                list.add(service);
            }
        } catch (SQLException e) {
            throw new DbConnectionException("List services database error", e);
        }

        return list;
    }

    @Override
    public List<Service> getServicesList() throws DbConnectionException {
        List<Service> list = new ArrayList<>();
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.GET_ALL_SERVICES_LIST);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Service service = getServiceFromResultSet(resultSet);
                list.add(service);
            }
        } catch (SQLException e) {
            throw new DbConnectionException("List services database error", e);
        }

        return list;
    }

    @Override
    public Integer getServicesCount(Map<String, String> parameters) throws DbConnectionException {
        QueryBuilder queryBuilder = new QueryBuilder(Queries.GET_SERVICES_COUNT, parameters);
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryBuilder.buildOnlySearch());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new DbConnectionException("Count services database error", e);
        }
        return null;
    }



    private Service getServiceFromResultSet(ResultSet resultSet) throws SQLException {

        return  new Service(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3));
    }

}
