package repository.impl;

import connector.DbConnectionPool;
import dao.IDao;
import repository.IServicesRepository;
import entity.Service;
import java.sql.*;
import java.util.*;

public class ServicesRepositoryImpl implements IServicesRepository {

    private final IDao serviceDao;

    public ServicesRepositoryImpl(IDao serviceDao) {
        this.serviceDao = serviceDao;
    }

    @Override
    public int addService(Service service) throws SQLException {

        try (Connection connection = DbConnectionPool.getConnection()) {
            Optional<Service> result = serviceDao.add(connection, service);
            result.orElseThrow(SQLException::new);
            return result.get().getId();
        }
    }

    @Override
    public Service getServiceById(int id) throws NoSuchElementException, SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            Optional<Service> result = serviceDao.get(connection, id);
            result.orElseThrow(NoSuchElementException::new);
            return result.get();
        }
    }

    @Override
    public boolean isServiceNameExist(String serviceName) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            List<Service> list = serviceDao.getList(connection, null);
            return list.stream().anyMatch(a -> a.getName().equals(serviceName));
        }
    }

    @Override
    public void updateService(Service service) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            serviceDao.update(connection, service);
        }
    }

    @Override
    public void deleteService(int id) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            serviceDao.delete(connection, id);
        }
    }

    @Override
    public List<Service> getServicesList(Map<String, String> parameters) throws SQLException {

        try (Connection connection = DbConnectionPool.getConnection()) {
            return serviceDao.getList(connection, parameters);
        }
    }

    @Override
    public Integer getServicesCount(Map<String, String> parameters) throws SQLException {

        try (Connection connection = DbConnectionPool.getConnection()) {
            return serviceDao.getCount(connection, parameters);
        }
    }

}
