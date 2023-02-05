package repository.impl;

import connector.DbConnectionPool;
import dao.IDao;
import repository.*;
import entity.UserTariff;
import enums.SubscribeStatus;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class UserTariffRepositoryImpl implements IUserTariffRepository {

    private final IDao userTariffDao;
    private static final Logger logger = LogManager.getLogger(TariffRepositoryImpl.class);

    public UserTariffRepositoryImpl(IDao userTariffDao) {

        this.userTariffDao = userTariffDao;
    }

    @Override
    public UserTariff addUserTariff(UserTariff userTariff) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            Optional<UserTariff> result = userTariffDao.add(connection, userTariff);
            result.orElseThrow(SQLException::new);
            return result.get();
        }
    }

    @Override
    public int userTariffCount(int tariffId, int userId) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("whereColumn", "user_id");
            parameters.put("whereValue", String.valueOf(userId));
            List<UserTariff> userTariffList = userTariffDao.getList(connection, parameters);
            long count = userTariffList.stream()
                    .filter(e -> e.getTariff().getId() == tariffId)
                    .count();
            return (int) count;
        }
    }

    @Override
    public UserTariff getUserTariff(int tariffId, int userId) throws NoSuchElementException, SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("whereColumn", "user_id");
            parameters.put("whereValue", String.valueOf(userId));
            List<UserTariff> userTariffList = userTariffDao.getList(connection, parameters);
            Optional<UserTariff> userTariff = userTariffList.stream().filter(e -> e.getTariff().getId() == tariffId).findFirst();
            userTariff.orElseThrow(NoSuchElementException::new);
            return userTariff.get();
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
            parameters.put("whereColumn", "user_id");
            parameters.put("whereValue", String.valueOf(userId));
            List<UserTariff> userTariffList = userTariffDao.getList(connection, parameters);
            return userTariffList.stream().filter(e -> e.getTariff().getService().getId() == serviceId).toList();
        }
    }

    @Override
    public List<UserTariff> getUserActiveTariffList(int userId, Map<String, String> parameters) throws SQLException {

        try (Connection connection = DbConnectionPool.getConnection()) {
            parameters.put("whereColumn", "user_id");
            parameters.put("whereValue", String.valueOf(userId));
            return userTariffDao.getList(connection, parameters);
        }
    }

    @Override
    public List<UserTariff> getAllActiveTariffList() throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("whereColumn", "status");
            parameters.put("whereValue", "ACTIVE");
            return userTariffDao.getList(connection, parameters);
        }
    }

    @Override
    public int getUserActiveTariffCount(int userId) throws SQLException {

        try (Connection connection = DbConnectionPool.getConnection()) {

            Map<String, String> parameters = new HashMap<>();
            parameters.put("whereColumn", "user_id");
            parameters.put("whereValue", String.valueOf(userId));
            List<UserTariff> userTariffList = userTariffDao.getList(connection, parameters);
            long count = userTariffList.stream()
                    .filter(e -> !e.getSubscribeStatus().equals(SubscribeStatus.UNSUBSCRIBE))
                    .count();
            return (int) count;
        }
    }

    @Override
    public List<UserTariff> getAllExpiredUserActiveTariffList() throws SQLException {

        try (Connection connection = DbConnectionPool.getConnection()) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("whereColumn", "status");
            parameters.put("whereValue", "ACTIVE");
            List<UserTariff> userTariffList = userTariffDao.getList(connection, parameters);
            return  userTariffList.stream().filter(e->e.getDateEnd().isBefore(LocalDate.now())).toList();
        }
    }

    @Override
    public List<UserTariff> getSubscribedUserTariffList(int userId) throws SQLException {

        try (Connection connection = DbConnectionPool.getConnection()) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("whereColumn", "user_id");
            parameters.put("whereValue", String.valueOf(userId));
            List<UserTariff> userTariffList = userTariffDao.getList(connection, parameters);
            return userTariffList.stream()
                    .filter(e -> e.getSubscribeStatus().equals(SubscribeStatus.ACTIVE) || e.getSubscribeStatus().equals(SubscribeStatus.PAUSED))
                    .toList();
        }
    }

    @Override
    public List<UserTariff> getBlockedUserTariffList(int userId) throws SQLException {

        try (Connection connection = DbConnectionPool.getConnection()) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("whereColumn", "user_id");
            parameters.put("whereValue", String.valueOf(userId));
            List<UserTariff> userTariffList = userTariffDao.getList(connection, parameters);
            return userTariffList.stream()
                    .filter(e -> e.getSubscribeStatus().equals(SubscribeStatus.BLOCKED))
                    .toList();
        }
    }

    @Override
    public List<UserTariff> getTariffSubscribersList(int tariffId) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("whereColumn", "tarifs_id");
            parameters.put("whereValue", String.valueOf(tariffId));
            List<UserTariff> userTariffList = userTariffDao.getList(connection, parameters);
            return userTariffList.stream()
                    .filter(e -> e.getSubscribeStatus().equals(SubscribeStatus.ACTIVE) || e.getSubscribeStatus().equals(SubscribeStatus.PAUSED))
                    .toList();
        }
    }

    @Override
    public void deleteUserTariff(int tariffId) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            userTariffDao.delete(connection, tariffId);
        }
    }

}

