package repository.impl;

import connector.DbConnectionPool;
import dependecies.DependencyManager;
import repository.IPaymentRepository;
import repository.IUserRepository;
import dao.QueryBuilder;
import entity.Payment;
import entity.User;
import enums.PaymentType;
import exceptions.DbConnectionException;
import exceptions.NotEnoughBalanceException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import settings.Patterns;
import settings.Queries;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PaymentRepository implements IPaymentRepository {
    private static final IUserRepository userDao = DependencyManager.userRepo;
    private static final String pattern = Patterns.datePattern;
    private static final Logger logger = LogManager.getLogger(PaymentRepository.class);

    @Override
    public Payment addPayment(Payment payment) throws DbConnectionException, NotEnoughBalanceException {

        Connection connection = null;

        try {
            connection = DbConnectionPool.getConnection();
            DbConnectionPool.startTransaction(connection);
            PreparedStatement statement = connection.prepareStatement(Queries.INSERT_PAYMENT, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, payment.getUser().getId());
            statement.setString(2, String.valueOf(payment.getValue()));
            String dateInString = new SimpleDateFormat(pattern).format(payment.getData());
            statement.setString(3, dateInString);
            statement.setString(4, payment.getType().toString());
            statement.setString(5, payment.getDescription());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();

            if (!keys.next()) throw new DbConnectionException("Add payment database error");

            payment.setId(keys.getInt(1));

            statement = connection.prepareStatement(Queries.GET_USER_BALANCE);
            statement.setInt(1, payment.getUser().getId());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) throw new DbConnectionException("Get user balance database error");

            BigDecimal oldBalance = BigDecimal.valueOf(resultSet.getDouble(2));
            BigDecimal userBalance = payment.getType().updateBalance(oldBalance, payment.getValue());
            if (userBalance.compareTo(BigDecimal.ZERO) < 0) throw new NotEnoughBalanceException();

            statement = connection.prepareStatement(Queries.UPDATE_USER_BALANCE);
            statement.setString(1, String.valueOf(userBalance));
            statement.setInt(2, payment.getUser().getId());

            statement.executeUpdate();

            DbConnectionPool.commitTransaction(connection);
            connection.close();

        } catch (SQLException | DbConnectionException e) {
            try {
                DbConnectionPool.rollbackTransaction(connection);
                connection.close();
            } catch (DbConnectionException | SQLException ex) {
                logger.error(e.getMessage());
                throw new DbConnectionException("Add payment database error", e);
            }
            logger.error(e.getMessage());
            throw new DbConnectionException("Add payment database error", e);
        } catch (NotEnoughBalanceException e) {
            try {
                DbConnectionPool.rollbackTransaction(connection);
                connection.close();
            } catch (DbConnectionException | SQLException ex) {
                logger.error(e.getMessage());
                throw new DbConnectionException("Add payment database error", e);
            }
            logger.error(e.getMessage());
            throw new NotEnoughBalanceException("alert.notEnoughBalance");
        }
        return payment;
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

    private Payment getPaymentFromResultSet(ResultSet resultSet) throws SQLException, DbConnectionException {
        User user = userDao.getUserById(resultSet.getInt(2));

        return new Payment(resultSet.getInt(1), user, BigDecimal.valueOf(resultSet.getDouble(3)),
                resultSet.getDate(4), PaymentType.valueOf(resultSet.getString(5)),
                resultSet.getString(6));
    }
}
