package service;

import dto.DtoTariff;
import entity.Tariff;
import enums.SortOrder;
import exceptions.*;

import java.util.List;
import java.util.NoSuchElementException;

public interface ITariffsService {
    Tariff getTariff(int id) throws DbConnectionException, NoSuchElementException;

    Tariff addTariff(DtoTariff dtoTariff) throws IncorrectFormatException, DbConnectionException;

    Tariff updateTariff(DtoTariff dtoTariff) throws IncorrectFormatException, DbConnectionException;

    void deleteTariff(int tariffId) throws DbConnectionException, RelatedRecordsExistException;

    List<Tariff> getTariffsList(Integer limit, Integer total, Integer sortColumn, SortOrder sortOrder) throws DbConnectionException;
    List<Tariff> getTariffsUserList(Integer limit, Integer total, Integer sortColumn, SortOrder sortOrder, int userId) throws DbConnectionException;

    List<Tariff> getActiveTariffsUserList(int userId) throws DbConnectionException;
    List<Tariff> getPriceTariffsList() throws DbConnectionException;

    List<Tariff> getFindTariffsList(Integer limit, Integer total, Integer sortColumn, SortOrder sortOrder, int field, String criteria) throws DbConnectionException;
    List<Tariff> getFindTariffsUserList(Integer limit, Integer total, Integer sortColumn, SortOrder sortOrder, int field, String criteria, int userId) throws DbConnectionException;

    Integer getTariffsCount() throws DbConnectionException;

    Integer getFindTariffsCount(int field, String criteria) throws DbConnectionException;

    void setTariffStatus(int tariff, String status) throws DbConnectionException;

    void subscribeTariff(int tariff, int user) throws DbConnectionException, TariffAlreadySubscribedException, NotEnoughBalanceException;

    void unsubscribeTariff(int tariff, int user) throws DbConnectionException;

    void setTariffPrice(int tariff, String price) throws DbConnectionException, IncorrectFormatException;
}
