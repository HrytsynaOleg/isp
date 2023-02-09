package repository;

import entity.UserTariff;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface IUserTariffRepository {

    int userTariffCount(int tariff, int user) throws SQLException;

    void updateUserTariff(UserTariff userTariff) throws SQLException;

    List<UserTariff> getUserTariffListByService(int serviceId, int userId) throws SQLException;

    List<UserTariff> getUserActiveTariffList(int userId, Map<String, String> parameters) throws SQLException;

    List<UserTariff> getAllActiveTariffList() throws SQLException;

    int getUserActiveTariffCount(int userId) throws SQLException;

    List<UserTariff> getAllExpiredUserActiveTariffList() throws SQLException;

    List<UserTariff> getSubscribedUserTariffList(int userId) throws SQLException;

    List<UserTariff> getBlockedUserTariffList(int userId) throws SQLException;

    List<UserTariff> getTariffSubscribersList(int tariffId) throws SQLException;

    Optional<UserTariff> getUserTariff(int tariff, int user) throws NoSuchElementException, SQLException;
}
