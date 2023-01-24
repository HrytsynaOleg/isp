package dao;

import entity.Tariff;
import entity.UserTariff;
import enums.SubscribeStatus;
import exceptions.DbConnectionException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IUserTariffDao {

    int addUserTariff(int tariff, int user) throws DbConnectionException;

    int userTariffCount(int tariff, int user) throws DbConnectionException;

    LocalDate getUserTariffEndDate(int userTariffId) throws DbConnectionException;

    LocalDate getUserTariffStartDate(int userTariffId) throws DbConnectionException;

    void setUserTariffStatus(int userTariffId, SubscribeStatus status) throws DbConnectionException;

    void setUserTariffEndDate(int userTariffId, LocalDate date) throws DbConnectionException;

    SubscribeStatus getUserTariffStatus(int userTariffId) throws DbConnectionException;

    List<Tariff> getUserTariffListByService(int serviceId, int userId) throws DbConnectionException;

    List<UserTariff> getUserActiveTariffList(int userId, Map<String,String> parameters) throws DbConnectionException;

    List<UserTariff> getAllActiveTariffList() throws DbConnectionException;

    int getUserActiveTariffCount(int userId) throws DbConnectionException;

    List<UserTariff> getExpiredUserActiveTariffList() throws DbConnectionException;

    List<UserTariff> getSubscribedUserTariffList(int userId) throws DbConnectionException;
    List<UserTariff> getBlockedUserTariffList(int userId) throws DbConnectionException;
    List<UserTariff> getTariffSubscribersList(int tariffId) throws DbConnectionException;

    void deleteUserTariff(int tariff) throws DbConnectionException;

    Integer getUserTariffId(int tariff, int user) throws DbConnectionException;
}
