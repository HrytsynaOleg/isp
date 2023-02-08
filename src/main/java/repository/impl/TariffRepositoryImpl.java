package repository.impl;

import connector.DbConnectionPool;
import dao.IDao;
import entity.Payment;
import entity.User;
import entity.UserTariff;
import enums.IncomingPaymentType;
import enums.PaymentType;
import enums.SubscribeStatus;
import repository.ITariffRepository;
import entity.Tariff;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

public class TariffRepositoryImpl implements ITariffRepository {
    private final IDao<Tariff> tariffDao;
    private final IDao<UserTariff> userTariffDao;
    private final IDao<Payment> paymentDao;
    private final IDao<User> userDao;
    private static final Logger logger = LogManager.getLogger(TariffRepositoryImpl.class);

    public TariffRepositoryImpl(IDao<Tariff> tariffDao, IDao<UserTariff> userTariffDao, IDao<Payment> paymentDao, IDao<User> userDao) {
        this.tariffDao = tariffDao;
        this.userTariffDao = userTariffDao;
        this.paymentDao = paymentDao;
        this.userDao = userDao;
    }

    @Override
    public Tariff addTariff(Tariff tariff) throws SQLException {

        try (Connection connection = DbConnectionPool.getConnection()) {
            Optional<Tariff> result = tariffDao.add(connection, tariff);
            result.orElseThrow(SQLException::new);
            return result.get();
        }
    }

    @Override
    public Tariff getTariffById(int id) throws NoSuchElementException, SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            Optional<Tariff> tariff = tariffDao.get(connection, id);
            tariff.orElseThrow(NoSuchElementException::new);
            return tariff.get();
        }
    }

    @Override
    public boolean isTariffNameExist(String name) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("whereColumn", "tarif_name");
            parameters.put("whereValue", name);
            Optional<Tariff> tariff = tariffDao.getList(connection, parameters).stream().findFirst();
            return tariff.isPresent();
        }
    }

    @Override
    public void updateTariff(Tariff newTariff, Tariff oldTariff, List<UserTariff> subscribers) throws SQLException {

        Connection connection = null;
        try {
            connection = DbConnectionPool.startTransaction();
            for (UserTariff userTariff : subscribers) {
                User user = userTariff.getUser();
                userTariff.setSubscribeStatus(SubscribeStatus.PAUSED);
                BigDecimal currentUserBalance = user.getBalance();
                BigDecimal returnValue = userTariff.calcMoneyBackValue();
                Payment moneyBackPayment = new Payment(0, user, returnValue, new Date(), PaymentType.IN, IncomingPaymentType.MONEYBACK.getName());
                paymentDao.add(connection, moneyBackPayment);
                currentUserBalance = currentUserBalance.add(returnValue);
                BigDecimal withdrawValue = newTariff.getPrice();
                if (currentUserBalance.compareTo(withdrawValue) >= 0) {
                    LocalDate date = userTariff.getTariff().getPeriod().getNexDate(LocalDate.now());
                    String userTariffWithdrawDescription = String.format("%s tariff %s subscribed to %s",
                            userTariff.getTariff().getService().getName(),
                            userTariff.getTariff().getName(), date);
                    Payment withdraw = new Payment(0, user, withdrawValue, new Date(), PaymentType.OUT, userTariffWithdrawDescription);
                    paymentDao.add(connection, withdraw);
                    userTariff.setDateEnd(userTariff.getTariff().getPeriod().getNexDate(LocalDate.now()));
                    userTariff.setSubscribeStatus(SubscribeStatus.ACTIVE);
                    currentUserBalance = currentUserBalance.subtract(withdrawValue);
                }
                userTariffDao.update(connection, userTariff);
                user.setBalance(currentUserBalance);
                userDao.update(connection, user);
            }
            tariffDao.update(connection, newTariff);
            DbConnectionPool.commitTransaction(connection);
        } catch (SQLException e) {
            DbConnectionPool.rollbackTransaction(connection);
            throw new SQLException(e);
        }
    }

    @Override
    public void subscribeTariff(Tariff tariff, UserTariff newUserTariff, Optional<UserTariff> oldUserTariff) throws SQLException {
        Connection connection = null;
        try {
            connection = DbConnectionPool.startTransaction();
            User user = newUserTariff.getUser();
            BigDecimal currentUserBalance = user.getBalance();
            if (oldUserTariff.isPresent()) {
                BigDecimal returnValue = oldUserTariff.get().calcMoneyBackValue();
                Payment moneyBackPayment = new Payment(0, user, returnValue, new Date(), PaymentType.IN, IncomingPaymentType.MONEYBACK.getName());
                paymentDao.add(connection, moneyBackPayment);
                userTariffDao.delete(connection, oldUserTariff.get().getId());
                currentUserBalance = currentUserBalance.add(returnValue);
            }
            BigDecimal withdrawValue = newUserTariff.getTariff().getPrice();
            LocalDate date = newUserTariff.getTariff().getPeriod().getNexDate(LocalDate.now());
            String userTariffWithdrawDescription = String.format("%s tariff %s subscribed to %s",
                    newUserTariff.getTariff().getService().getName(),
                    newUserTariff.getTariff().getName(), date);
            Payment withdraw = new Payment(0, user, withdrawValue, new Date(), PaymentType.OUT, userTariffWithdrawDescription);
            paymentDao.add(connection, withdraw);
            currentUserBalance = currentUserBalance.subtract(withdrawValue);
            userTariffDao.add(connection, newUserTariff);
            user.setBalance(currentUserBalance);
            userDao.update(connection, user);

            DbConnectionPool.commitTransaction(connection);
        } catch (SQLException e) {
            DbConnectionPool.rollbackTransaction(connection);
            throw new SQLException(e);
        }
    }

    @Override
    public void unsubscribeTariff(UserTariff userTariff) throws SQLException {
        Connection connection = null;
        try {
            connection = DbConnectionPool.startTransaction();
            User user = userTariff.getUser();
            BigDecimal currentUserBalance = user.getBalance();
            BigDecimal returnValue = userTariff.calcMoneyBackValue();
            if (userTariff.getSubscribeStatus().equals(SubscribeStatus.ACTIVE) && returnValue.compareTo(BigDecimal.ZERO) > 0) {
                Payment moneyBackPayment = new Payment(0, user, returnValue, new Date(), PaymentType.IN, IncomingPaymentType.MONEYBACK.getName());
                paymentDao.add(connection, moneyBackPayment);
                userTariffDao.delete(connection, userTariff.getId());
                currentUserBalance = currentUserBalance.add(returnValue);
            }
            userTariffDao.delete(connection, userTariff.getId());
            user.setBalance(currentUserBalance);
            userDao.update(connection, user);
            DbConnectionPool.commitTransaction(connection);
        } catch (SQLException e) {
            DbConnectionPool.rollbackTransaction(connection);
            throw new SQLException(e);
        }
    }

    @Override
    public void deleteTariff(int tariffId) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            tariffDao.delete(connection, tariffId);
        }
    }

    @Override
    public List<Tariff> getTariffsList(Map<String, String> parameters) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            return tariffDao.getList(connection, parameters);
        }
    }

    @Override
    public List<Tariff> getPriceTariffsList() throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("whereColumn", "tarif_status");
            parameters.put("whereValue", "ACTIVE");
            return tariffDao.getList(connection, parameters);
        }
    }

    @Override
    public Integer getTariffsCount(Map<String, String> parameters) throws SQLException {

        try (Connection connection = DbConnectionPool.getConnection()) {
            return tariffDao.getCount(connection, parameters);
        }
    }

}
