package dao;

import dto.DtoTariff;
import entity.Tariff;
import exceptions.DbConnectionException;

import java.util.List;

public interface ITariffDao {
    int addTariff(Tariff tariff) throws DbConnectionException;
    Tariff getTariffByName(String name) throws DbConnectionException;
    Tariff getTariffById(int id) throws DbConnectionException;
    void updateTariff(DtoTariff dtoTariff) throws DbConnectionException;
    List<Tariff> getTariffsList(Integer limit, Integer total, Integer sort, String order) throws DbConnectionException;
    List<Tariff> getFindTariffsList(Integer limit, Integer total, Integer sort, String order, int field, String criteria) throws DbConnectionException;
    Integer getTariffsCount() throws DbConnectionException;
    Integer getFindTariffsCount(int field, String criteria) throws DbConnectionException;
    void setTariffStatus(int tariff, String status) throws DbConnectionException;
    void setTariffPrice(int tariff, String price) throws DbConnectionException;
}
