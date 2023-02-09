package repository.impl;

import connector.DbConnectionPool;
import dao.IDao;
import repository.*;
import entity.UserTariff;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class UserTariffRepositoryImpl implements IUserTariffRepository {

    private final IDao<UserTariff> userTariffDao;

    public UserTariffRepositoryImpl(IDao<UserTariff> userTariffDao) {

        this.userTariffDao = userTariffDao;
    }

    @Override
    public int userTariffCount(int tariffId, int userId) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("whereValue", "users_id=" + userId + " AND tarifs_id=" + tariffId);
            List<UserTariff> userTariffList = userTariffDao.getList(connection, parameters);
            return userTariffList.size();
        }
    }

    @Override
    public Optional<UserTariff> getUserTariff(int tariffId, int userId) throws NoSuchElementException, SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("whereValue", "users_id=" + userId + " AND tarifs_id=" + tariffId);
            List<UserTariff> userTariffList = userTariffDao.getList(connection, parameters);
            return userTariffList.stream().findFirst();
        }
    }

    @Override
    public void updateUserTariff(UserTariff userTariff) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            userTariffDao.update(connection, userTariff);
        }
    }

    @Override
    public List<UserTariff> getUserTariffListByService(int serviceId, int userId) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("whereValue", "users_id=" + userId);
            List<UserTariff> userTariffList = userTariffDao.getList(connection, parameters);
            return userTariffList.stream().filter(e -> e.getTariff().getService().getId() == serviceId).toList();
        }
    }

    @Override
    public List<UserTariff> getUserActiveTariffList(int userId, Map<String, String> parameters) throws SQLException {

        try (Connection connection = DbConnectionPool.getConnection()) {
            parameters.put("whereValue", "users_id=" + userId);
            return userTariffDao.getList(connection, parameters);
        }
    }

    @Override
    public List<UserTariff> getAllActiveTariffList() throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("whereValue", "status='ACTIVE'");
            return userTariffDao.getList(connection, parameters);
        }
    }

    @Override
    public int getUserActiveTariffCount(int userId) throws SQLException {

        try (Connection connection = DbConnectionPool.getConnection()) {

            Map<String, String> parameters = new HashMap<>();
            parameters.put("whereValue", "users_id=" + userId + " AND status<>'UNSUBSCRIBE'");
            List<UserTariff> userTariffList = userTariffDao.getList(connection, parameters);
            return userTariffList.size();
        }
    }

    @Override
    public List<UserTariff> getAllExpiredUserActiveTariffList() throws SQLException {

        try (Connection connection = DbConnectionPool.getConnection()) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("whereValue", "status='ACTIVE' AND date_end<='" + LocalDate.now() + "'");
            return userTariffDao.getList(connection, parameters);
//            return userTariffList.stream().filter(e -> e.getDateEnd().isBefore(LocalDate.now())).toList();
        }
    }

    @Override
    public List<UserTariff> getSubscribedUserTariffList(int userId) throws SQLException {

        try (Connection connection = DbConnectionPool.getConnection()) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("whereValue", "users_id=" + userId + " AND (status='ACTIVE' OR status='PAUSED')");
            return userTariffDao.getList(connection, parameters);
        }
    }

    @Override
    public List<UserTariff> getBlockedUserTariffList(int userId) throws SQLException {

        try (Connection connection = DbConnectionPool.getConnection()) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("whereValue", "users_id=" + userId + " AND status='BLOCKED'");
            return userTariffDao.getList(connection, parameters);
        }
    }

    @Override
    public List<UserTariff> getTariffSubscribersList(int tariffId) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("whereValue", "tarifs_id=" + tariffId + " AND (status='ACTIVE' OR status='PAUSED')");
            return userTariffDao.getList(connection, parameters);
        }
    }

}

