package dao;

import entity.Tariff;
import exceptions.DbConnectionException;

import java.util.List;

public interface IUserTariffDao {

    int addUserTariff(int tariff, int user) throws DbConnectionException;

    int userTariffCount(int tariff, int user) throws DbConnectionException;

    List<Tariff> userTariffListByService(int serviceId, int userId) throws DbConnectionException;
    List<Tariff> userActiveTariffList(int userId) throws DbConnectionException;

    void deleteUserTariff(int tariff, int user) throws DbConnectionException;
}
