package dao.impl;

import connector.DbConnectionPool;
import dao.IServiceDao;
import dao.ITariffDao;
import dto.DtoTariff;
import entity.Service;
import entity.Tariff;
import enums.BillingPeriod;
import enums.SubscribeStatus;
import enums.TariffStatus;
import exceptions.DbConnectionException;
import settings.Queries;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class TariffDaoImpl implements ITariffDao {
    IServiceDao services = new ServiceDaoImpl();

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
            throw new DbConnectionException("Find tariff database error", e);
        }
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
            throw new DbConnectionException("Find tariff database error", e);
        }
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
            throw new DbConnectionException("Update tariff database error", e);
        }
    }

    @Override
    public List<Tariff> getTariffsList(Integer limit, Integer total, Integer sort, String order) throws DbConnectionException {
        List<Tariff> list = new ArrayList<>();
        try (Connection connection = DbConnectionPool.getConnection()) {
            String queryString = String.format(Queries.GET_TARIFFS_LIST, order);
            PreparedStatement statement = connection.prepareStatement(queryString);
            statement.setInt(1, sort);
            statement.setInt(2, limit);
            statement.setInt(3, total);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Tariff tariff = getTariffFromResultSet(resultSet);
                list.add(tariff);
            }
        } catch (SQLException e) {
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
            throw new DbConnectionException("List tariffs database error", e);
        }

        return list;
    }

    @Override
    public List<Tariff> getTariffsUserList(Integer limit, Integer total, Integer sort, String order, int userId) throws DbConnectionException {
        List<Tariff> list = new ArrayList<>();
        try (Connection connection = DbConnectionPool.getConnection()) {
            String queryString = String.format(Queries.GET_USER_TARIFFS_LIST, order);
            PreparedStatement statement = connection.prepareStatement(queryString);
            statement.setInt(1, userId);
            statement.setInt(2, sort);
            statement.setInt(3, limit);
            statement.setInt(4, total);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Tariff tariff = getTariffFromResultSet(resultSet);
                SubscribeStatus subscribeStatus = resultSet.getString(8) != null ?
                        SubscribeStatus.valueOf(resultSet.getString(8)) : SubscribeStatus.UNSUBSCRIBE;
                tariff.setSubscribe(subscribeStatus);
                list.add(tariff);
            }
        } catch (SQLException e) {
            throw new DbConnectionException("List tariffs database error", e);
        }

        return list;
    }



    @Override
    public List<Tariff> getFindTariffsList(Integer limit, Integer total, Integer sort, String order, int field, String criteria) throws DbConnectionException {
        String columnName = getColumnNameByIndex(field);
        List<Tariff> list = new ArrayList<>();
        try (Connection connection = DbConnectionPool.getConnection()) {
            String queryString = String.format(Queries.GET_FIND_TARIFFS_LIST, columnName, order);
            PreparedStatement statement = connection.prepareStatement(queryString);
            String queryCriteria = "%" + criteria + "%";
            statement.setString(1, queryCriteria);
            statement.setInt(2, sort);
            statement.setInt(3, limit);
            statement.setInt(4, total);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Tariff tariff = getTariffFromResultSet(resultSet);
                list.add(tariff);
            }
        } catch (SQLException e) {
            throw new DbConnectionException("List find tariffs database error", e);
        }

        return list;
    }

    @Override
    public List<Tariff> getFindTariffsUserList(Integer limit, Integer total, Integer sort, String order, int field, String criteria, int userId) throws DbConnectionException {
        String columnName = getColumnNameByIndex(field);
        List<Tariff> list = new ArrayList<>();
        try (Connection connection = DbConnectionPool.getConnection()) {
            String queryString = String.format(Queries.GET_FIND_USER_TARIFFS_LIST, columnName, order);
            PreparedStatement statement = connection.prepareStatement(queryString);
            String queryCriteria = "%" + criteria + "%";
            statement.setInt(1, userId);
            statement.setString(2, queryCriteria);
            statement.setInt(3, sort);
            statement.setInt(4, limit);
            statement.setInt(5, total);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Tariff tariff = getTariffFromResultSet(resultSet);
                SubscribeStatus subscribeStatus = resultSet.getString(8) != null ?
                        SubscribeStatus.valueOf(resultSet.getString(8)) : SubscribeStatus.UNSUBSCRIBE;
                tariff.setSubscribe(subscribeStatus);
                list.add(tariff);
            }
        } catch (SQLException e) {
            throw new DbConnectionException("List find tariffs database error", e);
        }

        return list;
    }

    @Override
    public Integer getTariffsCount() throws DbConnectionException {

        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.GET_TARIFFS_COUNT);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new DbConnectionException("Count tariffs database error", e);
        }
        return null;
    }


    @Override
    public Integer getFindTariffsCount(int field, String criteria) throws DbConnectionException {

        String columnName = getColumnNameByIndex(field);

        try (Connection connection = DbConnectionPool.getConnection()) {
            String queryString = String.format(Queries.GET_FIND_TARIFFS_COUNT, columnName);
            PreparedStatement statement = connection.prepareStatement(queryString);
            String queryCriteria = "%" + criteria + "%";
            statement.setString(1, queryCriteria);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
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
            throw new DbConnectionException("Set tarif price database error", e);
        }
    }


    private Tariff getTariffFromResultSet(ResultSet resultSet) throws SQLException, DbConnectionException {

        Service service = services.getServiceById(resultSet.getInt(2));

        return new Tariff(resultSet.getInt(1), service, resultSet.getString(3),
                resultSet.getString(5), BigDecimal.valueOf(resultSet.getDouble(4)),
                BillingPeriod.valueOf(resultSet.getString(6)), TariffStatus.valueOf(resultSet.getString(7)));
    }

    private String getColumnNameByIndex(int index) throws DbConnectionException {

        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(Queries.GET_COLUMN_NAME_BY_INDEX);
            statement.setString(1, "tarifs");
            statement.setInt(2, index);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            throw new DbConnectionException("Get column name database error", e);
        }
        return null;

    }
}
