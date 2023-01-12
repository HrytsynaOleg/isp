package service;

import dto.DtoTariff;
import entity.Tariff;
import enums.SortOrder;
import exceptions.DbConnectionException;
import exceptions.IncorrectFormatException;

import java.util.List;
import java.util.NoSuchElementException;

public interface ITariffsService {
    Tariff getTariff(int id) throws DbConnectionException, NoSuchElementException;

    Tariff addTariff(DtoTariff dtoTariff) throws IncorrectFormatException, DbConnectionException;

    Tariff updateTariff(DtoTariff dtoTariff) throws IncorrectFormatException, DbConnectionException;

    List<Tariff> getTariffsList(Integer limit, Integer total, Integer sortColumn, SortOrder sortOrder) throws DbConnectionException;

    List<Tariff> getFindTariffsList(Integer limit, Integer total, Integer sortColumn, SortOrder sortOrder, int field, String criteria) throws DbConnectionException;

    Integer getTariffsCount() throws DbConnectionException;

    Integer getFindTariffsCount(int field, String criteria) throws DbConnectionException;

    void setTariffStatus(int tariff, String status) throws DbConnectionException;

    void setTariffPrice(int tariff, String price) throws DbConnectionException, IncorrectFormatException;
}
