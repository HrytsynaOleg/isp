package dao;

import entity.Tariff;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public interface ITariffDao {
    Tariff addTariff(Tariff tariff) throws SQLException;

    Tariff getTariffById(int id) throws NoSuchElementException, SQLException;

    boolean isTariffNameExist(String name) throws SQLException;

    void updateTariff(Tariff tariff) throws SQLException;

    void deleteTariff(int tariffId) throws SQLException;

    List<Tariff> getTariffsList(Map<String, String> parameters) throws SQLException;

    List<Tariff> getPriceTariffsList() throws SQLException;

    Integer getTariffsCount(Map<String, String> parameters) throws SQLException;

}
