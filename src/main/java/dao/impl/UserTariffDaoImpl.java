package dao.impl;

import connector.DbConnectionPool;
import dao.IServiceDao;
import dao.ITariffDao;
import dao.IUserDao;
import dao.IUserTariffDao;
import entity.Service;
import entity.Tariff;
import entity.User;
import entity.UserTariff;
import enums.BillingPeriod;
import enums.SubscribeStatus;
import enums.TariffStatus;
import exceptions.DbConnectionException;
import settings.Queries;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserTariffDaoImpl implements IUserTariffDao {
    IServiceDao services = new ServiceDaoImpl();
    ITariffDao tariffDao = new TariffDaoImpl();
    IUserDao userDao = new UserDaoImpl();

    @Override
    public int addUserTariff(int tariffId, int user) throws DbConnectionException {
        try (Connection connection = DbConnectionPool.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(Queries.INSERT_USER_TARIFF, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, user);
            statement.setInt(2, tariffId);
            statement.setString(3, String.valueOf(TariffStatus.ACTIVE));

            String pattern = "yyyy-MM-dd H:m:s.S";
            String dateInString = new SimpleDateFormat(pattern).format(Date.valueOf(LocalDate.now()));
            statement.setString(4, dateInString);

            Tariff tariff = tariffDao.getTariffById(tariffId);
            LocalDate date = tariff.getPeriod().getNexDate(LocalDate.now());
            String dateEndString = new SimpleDateFormat(pattern).format(Date.valueOf(date));

            statement.setString(5, dateEndString);

            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            return keys.getInt(1);

        } catch (SQLException e) {
            throw new DbConnectionException("Add user tariff database error", e);
        }
    }

    @Override
    public int userTariffCount(int tariff, int user) throws DbConnectionException {
        try (Connection connection = DbConnectionPool.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(Queries.GET_USER_TARIFF_COUNT);
            statement.setInt(1, user);
            statement.setInt(2, tariff);
            statement.executeQuery();
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new DbConnectionException("Get user tariff count database error", e);
        }
        return 0;
    }

    @Override
    public LocalDate getUserTariffEndDate(int userTariffId) throws DbConnectionException {
        try (Connection connection = DbConnectionPool.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(Queries.GET_USER_TARIFF_END_DATE);
            statement.setInt(1, userTariffId);
            statement.executeQuery();
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDate(1).toLocalDate();
            }
        } catch (SQLException e) {
            throw new DbConnectionException("Get user tariff count database error", e);
        }
        return null;
    }

    public LocalDate getUserTariffStartDate(int userTariffId) throws DbConnectionException {
        try (Connection connection = DbConnectionPool.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(Queries.GET_USER_TARIFF_START_DATE);
            statement.setInt(1, userTariffId);
            statement.executeQuery();
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDate(1).toLocalDate();
            }
        } catch (SQLException e) {
            throw new DbConnectionException("Get user tariff count database error", e);
        }
        return null;
    }


    @Override
    public Integer getUserTariffId(int tariff, int user) throws DbConnectionException {
        try (Connection connection = DbConnectionPool.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(Queries.GET_USER_TARIFF);
            statement.setInt(1, user);
            statement.setInt(2, tariff);
            statement.executeQuery();
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new DbConnectionException("Get user tariff count database error", e);
        }
        return null;
    }

    @Override
    public void setUserTariffStatus(int userTariffId, SubscribeStatus status) throws DbConnectionException {
        try (Connection connection = DbConnectionPool.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(Queries.UPDATE_USER_TARIFF_STATUS);
            statement.setString(1, status.toString());
            statement.setInt(2, userTariffId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DbConnectionException("Update user tariff status database error", e);
        }
    }

    @Override
    public void setUserTariffEndDate(int userTariffId, LocalDate date) throws DbConnectionException {
        String pattern = "yyyy-MM-dd H:m:s.S";
        String dateEndString = new SimpleDateFormat(pattern).format(Date.valueOf(date));
        try (Connection connection = DbConnectionPool.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(Queries.UPDATE_USER_TARIFF_END_DATE);
            statement.setString(1, dateEndString);
            statement.setInt(2, userTariffId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DbConnectionException("Update user tariff end date database error", e);
        }
    }

    @Override
    public SubscribeStatus getUserTariffStatus(int userTariffId) throws DbConnectionException {
        try (Connection connection = DbConnectionPool.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(Queries.GET_USER_TARIFF_STATUS);
            statement.setInt(1, userTariffId);
            statement.executeQuery();
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return SubscribeStatus.valueOf(resultSet.getString(1));
            }
        } catch (SQLException e) {
            throw new DbConnectionException("Get user tariff count database error", e);
        }
        return null;
    }

    @Override
    public List<Tariff> getUserTariffListByService(int serviceId, int userId) throws DbConnectionException {
        List<Tariff> tariffList = new ArrayList<>();
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.GET_USER_TARIFFS_BY_SERVICE_ID);
            statement.setInt(1, serviceId);
            statement.setInt(2, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Tariff tariff = getTariffFromResultSet(resultSet);
                tariffList.add(tariff);
            }
        } catch (SQLException e) {
            throw new DbConnectionException("Get user tariff list database error", e);
        }
        return tariffList;
    }

    @Override
    public List<Tariff> getUserActiveTariffList(int userId) throws DbConnectionException {
        List<Tariff> tariffList = new ArrayList<>();
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.GET_ACTIVE_USER_TARIFFS);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Tariff tariff = getTariffFromResultSet(resultSet);
                tariff.setSubscribe(SubscribeStatus.valueOf(resultSet.getString(8)));
                tariff.setDateEnd(resultSet.getDate(9));
                tariffList.add(tariff);
            }
        } catch (SQLException e) {
            throw new DbConnectionException("Get user tariff list database error", e);
        }
        return tariffList;
    }

    @Override
    public List<UserTariff> getExpiredUserActiveTariffList() throws DbConnectionException {
        List<UserTariff> tariffList = new ArrayList<>();
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.GET_EXPIRED_ACTIVE_USER_TARIFFS);
            String pattern = "yyyy-MM-dd H:m:s.S";
            String dateInString = new SimpleDateFormat(pattern).format(Date.valueOf(LocalDate.now()));
            statement.setString(1, dateInString);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                UserTariff tariff = getUserTariffFromResultSet(resultSet);
                tariffList.add(tariff);
            }
        } catch (SQLException e) {
            throw new DbConnectionException("Get user tariff list database error", e);
        }
        return tariffList;
    }

    @Override
    public List<UserTariff> getSubscribedUserTariffList(int userId) throws DbConnectionException {
        List<UserTariff> tariffList = new ArrayList<>();
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.GET_SUBSCRIBED_USER_TARIFFS);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                UserTariff tariff = getUserTariffFromResultSet(resultSet);
                tariffList.add(tariff);
            }
        } catch (SQLException e) {
            throw new DbConnectionException("Get user subscribed tariff list database error", e);
        }
        return tariffList;
    }

    @Override
    public List<UserTariff> getBlockedUserTariffList(int userId) throws DbConnectionException {
        List<UserTariff> tariffList = new ArrayList<>();
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.GET_BLOCKED_USER_TARIFFS);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                UserTariff tariff = getUserTariffFromResultSet(resultSet);
                tariffList.add(tariff);
            }
        } catch (SQLException e) {
            throw new DbConnectionException("Get user subscribed tariff list database error", e);
        }
        return tariffList;
    }


    @Override
    public void deleteUserTariff(int tariff) throws DbConnectionException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.DELETE_USER_TARIFF);
            statement.setInt(1, tariff);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DbConnectionException("Delete user tariff database error", e);
        }
    }

    private Tariff getTariffFromResultSet(ResultSet resultSet) throws SQLException, DbConnectionException {

        Service service = services.getServiceById(resultSet.getInt(2));

        return new Tariff(resultSet.getInt(1), service, resultSet.getString(3),
                resultSet.getString(5), BigDecimal.valueOf(resultSet.getDouble(4)),
                BillingPeriod.valueOf(resultSet.getString(6)), TariffStatus.valueOf(resultSet.getString(7)));
    }

    private UserTariff getUserTariffFromResultSet(ResultSet resultSet) throws SQLException, DbConnectionException {
        User user = userDao.getUserById(resultSet.getInt(2));
        Tariff tariff = tariffDao.getTariffById(resultSet.getInt(3));
        return new UserTariff(resultSet.getInt(1), user, tariff,
                SubscribeStatus.valueOf(resultSet.getString(4)),
                resultSet.getDate(5).toLocalDate(), resultSet.getDate(6).toLocalDate());
    }
}

