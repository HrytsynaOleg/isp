package service;

import dto.DtoTable;
import dto.DtoTariff;
import entity.Tariff;
import entity.UserTariff;
import exceptions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

public interface ITariffsService {
    Tariff getTariff(int id) throws DbConnectionException, NoSuchElementException;

    Tariff addTariff(DtoTariff dtoTariff) throws IncorrectFormatException, DbConnectionException;

    Tariff updateTariff(DtoTariff dtoTariff) throws IncorrectFormatException, DbConnectionException;

    void deleteTariff(int tariffId) throws DbConnectionException, RelatedRecordsExistException;

    List<Tariff> getTariffsList(DtoTable dtoTable) throws DbConnectionException;

    List<Tariff> getTariffsUserList(int userId,DtoTable dtoTable) throws DbConnectionException;

    List<UserTariff> getActiveTariffsUserList(int userId, DtoTable dtoTable) throws DbConnectionException;

    int getActiveTariffsUserCount(int userId) throws DbConnectionException;

    List<Tariff> getPriceTariffsList() throws DbConnectionException;

    Integer getTariffsCount(DtoTable dtoTable) throws DbConnectionException;

    void setTariffStatus(int tariff, String status) throws DbConnectionException;

    void subscribeTariff(int tariff, int user) throws DbConnectionException, TariffAlreadySubscribedException, NotEnoughBalanceException;

    void unsubscribeTariff(int tariff, int user) throws DbConnectionException;

    void setTariffPrice(int tariff, String price) throws DbConnectionException, IncorrectFormatException;

    BigDecimal calcMonthTotalUserExpenses(int userId) throws DbConnectionException;

    BigDecimal calcMonthTotalProfit() throws DbConnectionException;
}
