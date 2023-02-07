package repository;

import entity.Tariff;
import entity.UserTariff;
import exceptions.NotEnoughBalanceException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface ITariffRepository {
    Tariff addTariff(Tariff tariff) throws SQLException;

    Tariff getTariffById(int id) throws NoSuchElementException, SQLException;

    boolean isTariffNameExist(String name) throws SQLException;

    void updateTariff(Tariff newTariff, Tariff oldTariff,List<UserTariff> subscribers) throws SQLException;

    void subscribeTariff(Tariff tariff, UserTariff newUserTariff, Optional<UserTariff> oldUserTariff) throws SQLException, NotEnoughBalanceException;

    void deleteTariff(int tariffId) throws SQLException;

    List<Tariff> getTariffsList(Map<String, String> parameters) throws SQLException;

    List<Tariff> getPriceTariffsList() throws SQLException;

    Integer getTariffsCount(Map<String, String> parameters) throws SQLException;

}
