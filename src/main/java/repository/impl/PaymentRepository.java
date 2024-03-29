package repository.impl;

import connector.DbConnectionPool;
import dao.IDao;
import entity.UserTariff;
import enums.SubscribeStatus;
import repository.IPaymentRepository;
import entity.Payment;
import entity.User;
import enums.PaymentType;
import exceptions.NotEnoughBalanceException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

public class PaymentRepository implements IPaymentRepository {

    private final IDao<Payment> paymentDao;
    private final IDao<User> userDao;
    private final IDao<UserTariff> userTariffDao;
    private static final Logger logger = LogManager.getLogger(PaymentRepository.class);

    public PaymentRepository(IDao<Payment> paymentDao, IDao<User> userDao, IDao<UserTariff> userTariffDao) {
        this.paymentDao = paymentDao;
        this.userDao = userDao;
        this.userTariffDao = userTariffDao;
    }

    @Override
    public Payment addPayment(Payment payment, List<UserTariff> pausedTariffs) throws SQLException {

        Connection connection = null;

        try {
            connection = DbConnectionPool.startTransaction();
            User user = payment.getUser();
            Optional<Payment> newPayment = paymentDao.add(connection, payment);
            newPayment.orElseThrow(SQLException::new);
            BigDecimal newBalance = payment.getType().calculateBalance(user.getBalance(), payment.getValue());
            user.setBalance(newBalance);
            userDao.update(connection, user);
            BigDecimal paymentValue = payment.getValue();
            for (UserTariff userTariff : pausedTariffs) {
                BigDecimal withdrawValue = userTariff.getTariff().getPrice();
                if (paymentValue.compareTo(withdrawValue) >= 0) {
                    LocalDate date = userTariff.getTariff().getPeriod().getNexDate(LocalDate.now());
                    String userTariffWithdrawDescription = String.format("%s tariff %s subscribed to %s",
                            userTariff.getTariff().getService().getName(),
                            userTariff.getTariff().getName(), date);
                    Payment withdraw = new Payment(0, user, withdrawValue, new Date(), PaymentType.OUT, userTariffWithdrawDescription);
                    paymentDao.add(connection, withdraw);
                    Optional<User> newUser = userDao.get(connection, user.getId());
                    newUser.orElseThrow(SQLException::new);
                    BigDecimal userBalance = withdraw.getType().calculateBalance(newUser.get().getBalance(), withdrawValue);
                    user.setBalance(userBalance);
                    userDao.update(connection, user);
                    userTariff.setSubscribeStatus(SubscribeStatus.ACTIVE);
                    userTariff.setDateEnd(date);
                    userTariffDao.update(connection, userTariff);
                    paymentValue = paymentValue.subtract(withdrawValue);
                }
            }
            DbConnectionPool.commitTransaction(connection);
            return newPayment.get();
        } catch (SQLException e) {
            DbConnectionPool.rollbackTransaction(connection);
            logger.error(e.getMessage());
            throw new SQLException(e);
        }
    }


    @Override
    public Payment addWithdraw(Payment withdraw, UserTariff userTariff) throws NotEnoughBalanceException, SQLException {
        Connection connection = null;
        try {
            connection = DbConnectionPool.startTransaction();
            User user = withdraw.getUser();
            Optional<Payment> newWithdraw = paymentDao.add(connection, withdraw);
            newWithdraw.orElseThrow(SQLException::new);
            BigDecimal newBalance = withdraw.getType().calculateBalance(user.getBalance(), withdraw.getValue());
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) throw new NotEnoughBalanceException();
            userTariff.setSubscribeStatus(SubscribeStatus.ACTIVE);
            LocalDate date = userTariff.getTariff().getPeriod().getNexDate(LocalDate.now());
            userTariff.setDateEnd(date);
            userTariffDao.update(connection, userTariff);
            user.setBalance(newBalance);
            userDao.update(connection, user);
            DbConnectionPool.commitTransaction(connection);
            return newWithdraw.get();
        } catch (SQLException e) {
            DbConnectionPool.rollbackTransaction(connection);
            logger.error(e.getMessage());
            throw new SQLException(e);
        } catch (NotEnoughBalanceException e) {
            DbConnectionPool.rollbackTransaction(connection);
            throw new NotEnoughBalanceException("alert.notEnoughBalance");
        }
    }

    @Override
    public List<Payment> getPaymentsListByUser(int userId, PaymentType type, Map<String, String> parameters) throws SQLException {

        try (Connection connection = DbConnectionPool.getConnection()) {
            String whereCriteria = "users_id=" + userId + " AND type='" + type + "'";
            parameters.put("whereValue", whereCriteria);
            return paymentDao.getList(connection, parameters);
        }
    }

    @Override
    public List<Payment> getPaymentsListAllUsers(PaymentType type, Map<String, String> parameters) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            String whereCriteria = "type='" + type + "'";
            parameters.put("whereValue", whereCriteria);
            return paymentDao.getList(connection, parameters);
        }
    }

    @Override
    public Integer getPaymentsCountByUserId(int userId, PaymentType type) throws SQLException {

        try (Connection connection = DbConnectionPool.getConnection()) {
            Map<String, String> parameters = new HashMap<>();
            String whereCriteria = "users_id=" + userId + " AND type='" + type + "'";
            parameters.put("whereValue", whereCriteria);
            List<Payment> list = paymentDao.getList(connection, parameters);
            return Math.toIntExact(list.size());

        }
    }

    @Override
    public Integer getPaymentsCountAllUsers(PaymentType type) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            Map<String, String> parameters = new HashMap<>();
            String whereCriteria = "type='" + type + "'";
            parameters.put("whereValue", whereCriteria);
            List<Payment> list = paymentDao.getList(connection, parameters);
            return Math.toIntExact(list.size());
        }
    }

}
