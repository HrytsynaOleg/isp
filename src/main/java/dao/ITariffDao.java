package dao;

import com.mysql.cj.conf.ConnectionUrlParser;
import dto.DtoTable;
import dto.DtoTariff;
import entity.Tariff;
import enums.SubscribeStatus;
import exceptions.DbConnectionException;

import java.util.List;
import java.util.Map;

public interface ITariffDao {
    int addTariff(Tariff tariff) throws DbConnectionException;

    Tariff getTariffByName(String name) throws DbConnectionException;

    Tariff getTariffById(int id) throws DbConnectionException;

    void updateTariff(DtoTariff dtoTariff) throws DbConnectionException;

    void deleteTariff(int tariffId) throws DbConnectionException;

    List<Tariff> getTariffsList(Map<String,String> parameters) throws DbConnectionException;

    List<Tariff> getPriceTariffsList() throws DbConnectionException;

    Integer getTariffsCountFindByField(String field, String criteria) throws DbConnectionException;

    Integer getTariffsCount(Map<String,String> parameters) throws DbConnectionException;


    void setTariffStatus(int tariff, String status) throws DbConnectionException;

    void setTariffPrice(int tariff, String price) throws DbConnectionException;
}
