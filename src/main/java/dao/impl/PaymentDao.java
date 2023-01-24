package dao.impl;

import connector.DbConnectionPool;
import dao.IPaymentDao;
import dao.IUserDao;
import dao.QueryBuilder;
import entity.Payment;
import entity.User;
import enums.PaymentType;
import exceptions.DbConnectionException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import service.impl.PaymentService;
import settings.Patterns;
import settings.Queries;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PaymentDao implements IPaymentDao {
    private static final IUserDao userDao = new UserDaoImpl();
    private static final String pattern = Patterns.datePattern;
    private static final Logger logger = LogManager.getLogger(PaymentDao.class);

    @Override
    public void addIncomingPayment(int userId, BigDecimal value, String description) throws DbConnectionException {

        Connection connection = null;

        try {
            connection = DbConnectionPool.getConnection();
            DbConnectionPool.startTransaction(connection);
            BigDecimal userBalance = null;
            PreparedStatement statement = connection.prepareStatement(Queries.INSERT_PAYMENT, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, userId);
            statement.setString(2, String.valueOf(value));
            String dateInString = new SimpleDateFormat(pattern).format(new Date());
            statement.setString(3, dateInString);
            statement.setString(4, PaymentType.IN.toString());
            statement.setString(5, description + " " + dateInString);
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (!keys.next()) throw new DbConnectionException("Add payment database error");

            statement = connection.prepareStatement(Queries.GET_USER_BALANCE);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                userBalance = BigDecimal.valueOf(resultSet.getDouble(2)).add(value);
            }

            statement = connection.prepareStatement(Queries.UPDATE_USER_BALANCE);
            statement.setString(1, String.valueOf(userBalance));
            statement.setInt(2, userId);

            statement.executeUpdate();

            DbConnectionPool.commitTransaction(connection);
            connection.close();

        } catch (SQLException | DbConnectionException e) {
            try {
                if (connection != null) DbConnectionPool.rollbackTransaction(connection);
            } catch (DbConnectionException ex) {
                logger.error(e.getMessage());
                throw new DbConnectionException("Add payment database error", e);
            }
            logger.error(e.getMessage());
            throw new DbConnectionException("Add payment database error", e);
        }

    }

    @Override
    public List<Payment> getPaymentsListByUser(int userId, PaymentType type, Map<String, String> parameters) throws DbConnectionException {
        String queryString = userId > 0 ? Queries.GET_USER_PAYMENTS_LIST : Queries.GET_ALL_PAYMENTS_LIST;
        List<Payment> list = new ArrayList<>();
        QueryBuilder queryBuilder = new QueryBuilder(queryString, parameters);
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryBuilder.build());
            statement.setString(1, type.toString());
            if (userId > 0) statement.setInt(2, userId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Payment payment = getPaymentFromResultSet(resultSet);
                list.add(payment);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DbConnectionException("List user payments database error", e);
        }

        return list;
    }

    @Override
    public Integer getPaymentsCountByUserId(int userId, PaymentType type) throws DbConnectionException {
        String queryString = userId > 0 ? Queries.GET_USER_PAYMENTS_COUNT : Queries.GET_ALL_PAYMENTS_COUNT;
        try (Connection connection = DbConnectionPool.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(queryString);
            statement.setString(1, type.toString());
            if (userId > 0) statement.setInt(2, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException | NoClassDefFoundError | ExceptionInInitializerError e) {
            logger.error(e.getMessage());
            throw new DbConnectionException("Count user payments database error", e);
        }
        return null;
    }

    @Override
    public boolean addWithdrawPayment(int userId, BigDecimal value, String description) throws DbConnectionException {
        Connection connection = null;

        try {
            connection = DbConnectionPool.getConnection();
            DbConnectionPool.startTransaction(connection);
            boolean result = false;
            PreparedStatement statement = connection.prepareStatement(Queries.GET_USER_BALANCE);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                BigDecimal userBalance = BigDecimal.valueOf(resultSet.getDouble(2));
                if (userBalance.compareTo(value) >= 0) {
                    statement = connection.prepareStatement(Queries.INSERT_PAYMENT, Statement.RETURN_GENERATED_KEYS);
                    statement.setInt(1, userId);
                    statement.setString(2, String.valueOf(value));
                    String dateInString = new SimpleDateFormat(pattern).format(new Date());
                    statement.setString(3, dateInString);
                    statement.setString(4, PaymentType.OUT.toString());
                    statement.setString(5, description);
                    statement.executeUpdate();
                    ResultSet keys = statement.getGeneratedKeys();
                    if (!keys.next()) throw new DbConnectionException("Write off  payment database error");

                    userBalance = BigDecimal.valueOf(resultSet.getDouble(2)).subtract(value);
                    statement = connection.prepareStatement(Queries.UPDATE_USER_BALANCE);
                    statement.setString(1, String.valueOf(userBalance));
                    statement.setInt(2, userId);
                    statement.executeUpdate();

                    result = true;
                }
            }
            DbConnectionPool.commitTransaction(connection);
            connection.close();
            return result;

        } catch (SQLException | DbConnectionException e) {
            try {
                if (connection != null) DbConnectionPool.rollbackTransaction(connection);
            } catch (DbConnectionException ex) {
                logger.error(e.getMessage());
                throw new DbConnectionException("Write off payment database error", e);
            }
            logger.error(e.getMessage());
            throw new DbConnectionException("Write off payment database error", e);
        }
    }

    private Payment getPaymentFromResultSet(ResultSet resultSet) throws SQLException, DbConnectionException {
        User user = userDao.getUserById(resultSet.getInt(2));

        return new Payment(resultSet.getInt(1), user, BigDecimal.valueOf(resultSet.getDouble(3)),
                resultSet.getDate(4), PaymentType.valueOf(resultSet.getString(5)),
                resultSet.getString(6));
    }
}
