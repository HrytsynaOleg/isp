package dao.impl;

import connector.DbConnectionPool;
import dao.IServiceDao;
import dao.ITariffDao;
import dao.QueryBuilder;
import dto.DtoTariff;
import entity.Service;
import entity.Tariff;
import enums.BillingPeriod;
import enums.SubscribeStatus;
import enums.TariffStatus;
import exceptions.DbConnectionException;
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
    public int addTariff(Tariff tariff) throws DbConnectionException {

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
            return keys.getInt(1);

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException("Add tariff database error", e);
        }
    }

    @Override
    public Tariff getTariffByName(String name) throws DbConnectionException, NoSuchElementException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.GET_TARIFF_BY_NAME);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getTariffFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException("Find tariff database error", e);
        }
        logger.error("Tariff not found");
        throw new NoSuchElementException("Tariff not found");
    }

    @Override
    public Tariff getTariffById(int id) throws DbConnectionException, NoSuchElementException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.GET_TARIFF_BY_ID);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getTariffFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException("Find tariff database error", e);
        }
        logger.error("Tariff not found");
        throw new NoSuchElementException("Tariff not found");
    }

    @Override
    public void updateTariff(DtoTariff dtoTariff) throws DbConnectionException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.UPDATE_TARIFF_BY_ID);
            statement.setString(1, dtoTariff.getName());
            statement.setString(2, dtoTariff.getPrice());
            statement.setString(3, dtoTariff.getDescription());
            statement.setString(4, dtoTariff.getPeriod());
            statement.setString(5, dtoTariff.getStatus());
            statement.setInt(6, Integer.parseInt(dtoTariff.getId()));
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException("Update tariff database error", e);
        }
    }

    @Override
    public void deleteTariff(int tariffId) throws DbConnectionException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.DELETE_TARIFF_BY_ID);
            statement.setInt(1, tariffId);
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException("Update tariff database error", e);
        }
    }

    @Override
    public List<Tariff> getTariffsList(Map<String,String> parameters) throws DbConnectionException {
        QueryBuilder queryBuilder = new QueryBuilder(Queries.GET_TARIFFS_LIST, parameters);
        List<Tariff> list = new ArrayList<>();
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryBuilder.build());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Tariff tariff = getTariffFromResultSet(resultSet);
                list.add(tariff);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException("List tariffs database error", e);
        }

        return list;
    }

    @Override
    public List<Tariff> getPriceTariffsList() throws DbConnectionException {
        List<Tariff> list = new ArrayList<>();
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.GET_PRICE_TARIFFS_LIST);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Tariff tariff = getTariffFromResultSet(resultSet);
                list.add(tariff);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException("List tariffs database error", e);
        }

        return list;
    }

    @Override
    public Integer getTariffsCount(Map<String,String> parameters) throws DbConnectionException {

        QueryBuilder queryBuilder = new QueryBuilder(Queries.GET_TARIFFS_COUNT, parameters);

        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryBuilder.buildOnlySearch());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException("Count tariffs database error", e);
        }
        return null;
    }

    @Override
    public Integer getTariffsCountFindByField(String field, String criteria) throws DbConnectionException {

        try (Connection connection = DbConnectionPool.getConnection()) {
            String queryString = String.format(Queries.GET_TARIFFS_COUNT_FIND_BY_FIELD, field);
            PreparedStatement statement = connection.prepareStatement(queryString);
            String queryCriteria = "%" + criteria + "%";
            statement.setString(1, queryCriteria);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException("Count tariffs database error", e);
        }
        return null;
    }


    @Override
    public void setTariffStatus(int tariff, String status) throws DbConnectionException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.UPDATE_TARIFF_STATUS);
            statement.setString(1, status);
            statement.setInt(2, tariff);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException("Set tarif status database error", e);
        }
    }


    @Override
    public void setTariffPrice(int tariff, String price) throws DbConnectionException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.UPDATE_TARIFF_PRICE);
            statement.setString(1, price);
            statement.setInt(2, tariff);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException("Set tarif price database error", e);
        }
    }


    private Tariff getTariffFromResultSet(ResultSet resultSet) throws SQLException, DbConnectionException {

        Service service = services.getServiceById(resultSet.getInt(2));

        return new Tariff(resultSet.getInt(1), service, resultSet.getString(3),
                resultSet.getString(5), BigDecimal.valueOf(resultSet.getDouble(4)),
                BillingPeriod.valueOf(resultSet.getString(6)), TariffStatus.valueOf(resultSet.getString(7)));
    }
}
