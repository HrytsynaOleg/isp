package dao.impl;

import connector.DbConnectionPool;
import dao.IServiceDao;
import dao.IUserTariffDao;
import entity.Service;
import entity.Tariff;
import enums.BillingPeriod;
import enums.SubscribeStatus;
import enums.TariffStatus;
import exceptions.DbConnectionException;
import settings.Queries;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserTariffDaoImpl implements IUserTariffDao {
    IServiceDao services = new ServiceDaoImpl();

    @Override
    public int addUserTariff(int tariff, int user) throws DbConnectionException {
        try (Connection connection = DbConnectionPool.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(Queries.INSERT_USER_TARIFF, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, user);
            statement.setInt(2, tariff);
            statement.setString(3, String.valueOf(TariffStatus.ACTIVE));

            String pattern = "yyyy-MM-dd H:m:s.S";
            String dateInString = new SimpleDateFormat(pattern).format(new Date());

            statement.setString(4, dateInString);
            statement.setString(5, dateInString);

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
    public List<Tariff> userTariffListByService(int serviceId, int userId) throws DbConnectionException {
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
    public List<Tariff> userActiveTariffList(int userId) throws DbConnectionException {
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
    public void deleteUserTariff(int tariff, int user) throws DbConnectionException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.DELETE_USER_TARIFF);
            statement.setInt(1, user);
            statement.setInt(2, tariff);
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
}

