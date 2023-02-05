package repository.impl;

import connector.DbConnectionPool;
import dao.IDao;
import repository.ITariffRepository;
import entity.Tariff;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.sql.*;
import java.util.*;

public class TariffRepositoryImpl implements ITariffRepository {
    private final IDao tariffDao;
    private static final Logger logger = LogManager.getLogger(TariffRepositoryImpl.class);

    public TariffRepositoryImpl(IDao tariffDao) {
        this.tariffDao = tariffDao;
    }

    @Override
    public Tariff addTariff(Tariff tariff) throws SQLException {

        try (Connection connection = DbConnectionPool.getConnection()) {
            Optional<Tariff> result = tariffDao.add(connection, tariff);
            result.orElseThrow(SQLException::new);
            return result.get();
        }
    }

    @Override
    public Tariff getTariffById(int id) throws NoSuchElementException, SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            Optional<Tariff> tariff = tariffDao.get(connection, id);
            tariff.orElseThrow(NoSuchElementException::new);
            return tariff.get();
        }
    }

    @Override
    public boolean isTariffNameExist(String name) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("whereColumn", "tarif_name");
            parameters.put("whereValue", name);
            Optional<Tariff> tariff = tariffDao.getList(connection, parameters).stream().findFirst();
            return tariff.isPresent();
        }
    }

    @Override
    public void updateTariff(Tariff tariff) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            tariffDao.update(connection, tariff);
        }
    }

    @Override
    public void deleteTariff(int tariffId) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            tariffDao.delete(connection, tariffId);
        }
    }

    @Override
    public List<Tariff> getTariffsList(Map<String, String> parameters) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            return tariffDao.getList(connection, parameters);
        }
    }

    @Override
    public List<Tariff> getPriceTariffsList() throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("whereColumn", "tarif_status");
            parameters.put("whereValue", "ACTIVE");
            return tariffDao.getList(connection, parameters);
        }
    }

    @Override
    public Integer getTariffsCount(Map<String, String> parameters) throws SQLException {

        try (Connection connection = DbConnectionPool.getConnection()) {
            return tariffDao.getCount(connection, parameters);
        }
    }

}
