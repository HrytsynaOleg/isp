package repository.impl;

import connector.DbConnectionPool;
import dao.IDao;
import entity.Payment;
import entity.UserTariff;
import enums.IncomingPaymentType;
import enums.PaymentType;
import enums.SubscribeStatus;
import repository.IUserRepository;
import entity.User;
import enums.UserStatus;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

public class UserRepositoryImpl implements IUserRepository {
    private final IDao userDao;
    private final IDao userTariffDao;
    private final IDao paymentDao;

    public UserRepositoryImpl(IDao userDao, IDao userTariffDao, IDao paymentDao) {
        this.userDao = userDao;
        this.userTariffDao = userTariffDao;
        this.paymentDao = paymentDao;
    }

    @Override
    public User addUser(User user) throws SQLException {

        try (Connection connection = DbConnectionPool.getConnection()) {
            Optional<User> result = userDao.add(connection, user);
            result.orElseThrow(SQLException::new);
            return result.get();
        }
    }

    @Override
    public User getUserByLogin(String login) throws NoSuchElementException, SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            Map<String, String> parameters = new HashMap<>();
            parameters.put("whereColumn", "user_email");
            parameters.put("whereValue", login);
            Optional<User> user = userDao.getList(connection, parameters).stream().findFirst();
            user.orElseThrow(NoSuchElementException::new);
            return user.get();
        }
    }

    @Override
    public User getUserById(int userId) throws NoSuchElementException, SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            Optional<User> user = userDao.get(connection, userId);
            user.orElseThrow(NoSuchElementException::new);
            return user.get();
        }
    }

    @Override
    public void updateUserProfile(User user) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            userDao.update(connection, user);
        }
    }

    @Override
    public List<User> getUsersList(Map<String, String> parameters) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            return userDao.getList(connection, parameters);
        }

    }

    @Override
    public Integer getUsersCount(Map<String, String> parameters) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            return userDao.getCount(connection, parameters);
        }
    }

    @Override
    public void setUserStatus(User user, UserStatus status) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            user.setStatus(status);
            userDao.update(connection, user);
        }
    }

    @Override
    public void setUserPassword(User user, String password) throws SQLException {
        try (Connection connection = DbConnectionPool.getConnection()) {
            user.setPassword(password);
            userDao.update(connection, user);
        }
    }

    @Override
    public void blockUser(User user, List<UserTariff> userTariffList) throws SQLException {
        Connection connection = null;
        try {
            connection = DbConnectionPool.startTransaction();
            for (UserTariff userTariff : userTariffList) {
                userTariff.setSubscribeStatus(SubscribeStatus.BLOCKED);
                userTariffDao.update(connection, userTariff);
            }
            List<UserTariff> userActiveTariffList = userTariffList.stream()
                    .filter(e -> e.getSubscribeStatus().equals(SubscribeStatus.ACTIVE))
                    .toList();
            BigDecimal currentUserBalance = user.getBalance();
            for (UserTariff userTariff : userActiveTariffList) {
                BigDecimal returnValue = userTariff.calcMoneyBackValue();
                Payment moneyBackPayment = new Payment(0, user, returnValue, new Date(), PaymentType.IN, IncomingPaymentType.MONEYBACK.getName());
                paymentDao.add(connection, moneyBackPayment);
                currentUserBalance.add(returnValue);
            }
            user.setStatus(UserStatus.BLOCKED);
            user.setBalance(currentUserBalance);
            userDao.update(connection, user);
            DbConnectionPool.commitTransaction(connection);
        } catch (SQLException e) {
            DbConnectionPool.rollbackTransaction(connection);
            throw new SQLException(e);
        }
    }

    @Override
    public void unblockUser(User user, List<UserTariff> userTariffList) throws SQLException {
        Connection connection = null;
        try {
            connection = DbConnectionPool.startTransaction();
            BigDecimal currentUserBalance = user.getBalance();
            for (UserTariff userTariff : userTariffList) {
                LocalDate date = userTariff.getTariff().getPeriod().getNexDate(LocalDate.now());
                String userTariffWithdrawDescription = String.format("%s tariff %s subscribed to %s",
                        userTariff.getTariff().getService().getName(),
                        userTariff.getTariff().getName(), date);
                BigDecimal withdrawValue = userTariff.getTariff().getPrice();
                SubscribeStatus userTariffStatus = SubscribeStatus.PAUSED;
                if (currentUserBalance.compareTo(withdrawValue)>=0){
                    Payment withdraw = new Payment(0, user, withdrawValue, new Date(), PaymentType.OUT, userTariffWithdrawDescription);
                    paymentDao.add(connection,withdraw);
                    currentUserBalance.subtract(withdrawValue);
                    userTariffStatus = SubscribeStatus.ACTIVE;
                }
                userTariff.setSubscribeStatus(userTariffStatus);
                userTariffDao.update(connection,userTariff);
            }
            user.setStatus(UserStatus.ACTIVE);
            user.setBalance(currentUserBalance);
            userDao.update(connection, user);
            DbConnectionPool.commitTransaction(connection);
        } catch (SQLException e) {
            DbConnectionPool.rollbackTransaction(connection);
            throw new SQLException(e);
        }
    }

}
