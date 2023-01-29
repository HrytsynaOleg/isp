package dao.impl;

import connector.DbConnectionPool;
import dao.IServiceDao;
import dao.ITariffDao;
import dao.QueryBuilder;
import entity.Service;
import entity.Tariff;
import enums.BillingPeriod;
import enums.TariffStatus;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import settings.Queries;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class TariffDaoImpl implements ITariffDao {
    private static final IServiceDao services = new ServiceDaoImpl();
    private static final Logger logger = LogManager.getLogger(TariffDaoImpl.class);

    @Override
    public Tariff addTariff(Tariff tariff) throws SQLException {

        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.INSERT_TARIFF, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, tariff.getService().getId());
            statement.setString(2, tariff.getName());
            statement.setString(3, String.valueOf(tariff.getPrice()));
            statement.setString(4, tariff.getDescription());
            statement.setString(5, String.valueOf(tariff.getPeriod()));
            statement.setString(6, String.valueOf(tariff.getStatus()));

            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            keys.next();

            tariff.setId(keys.getInt(1));
            return tariff;
        }
    }

    @Override
    public Tariff getTariffById(int id) throws NoSuchElementException, SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.GET_TARIFF_BY_ID);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getTariffFromResultSet(resultSet);
            }
        }
        logger.error("Tariff not found");
        throw new NoSuchElementException("Tariff not found");
    }

    @Override
    public boolean isTariffNameExist(String name) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.GET_TARIFF_BY_NAME);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateTariff(Tariff tariff) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.UPDATE_TARIFF_BY_ID);
            statement.setString(1, tariff.getName());
            statement.setString(2, String.valueOf(tariff.getPrice()));
            statement.setString(3, tariff.getDescription());
            statement.setString(4, tariff.getPeriod().toString());
            statement.setString(5, tariff.getStatus().toString());
            statement.setInt(6, tariff.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void deleteTariff(int tariffId) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.DELETE_TARIFF_BY_ID);
            statement.setInt(1, tariffId);
            statement.executeUpdate();
        }
    }

    @Override
    public List<Tariff> getTariffsList(Map<String,String> parameters) throws SQLException {
        QueryBuilder queryBuilder = new QueryBuilder(Queries.GET_TARIFFS_LIST, parameters);
        List<Tariff> list = new ArrayList<>();
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryBuilder.build());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Tariff tariff = getTariffFromResultSet(resultSet);
                list.add(tariff);
            }
        }

        return list;
    }

    @Override
    public List<Tariff> getPriceTariffsList() throws SQLException {
        List<Tariff> list = new ArrayList<>();
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.GET_PRICE_TARIFFS_LIST);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Tariff tariff = getTariffFromResultSet(resultSet);
                list.add(tariff);
            }
        }

        return list;
    }

    @Override
    public Integer getTariffsCount(Map<String,String> parameters) throws SQLException {

        QueryBuilder queryBuilder = new QueryBuilder(Queries.GET_TARIFFS_COUNT, parameters);

        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryBuilder.buildOnlySearch());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return null;
    }



    private Tariff getTariffFromResultSet(ResultSet resultSet) throws SQLException, NoSuchElementException {

        Service service = services.getServiceById(resultSet.getInt(2));

        return new Tariff(resultSet.getInt(1), service, resultSet.getString(3),
                resultSet.getString(5), BigDecimal.valueOf(resultSet.getDouble(4)),
                BillingPeriod.valueOf(resultSet.getString(6)), TariffStatus.valueOf(resultSet.getString(7)));
    }
}
