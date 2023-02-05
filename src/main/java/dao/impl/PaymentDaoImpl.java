package dao.impl;

import dao.IDao;
import dao.QueryBuilder;
import dependecies.DependencyManager;
import entity.Payment;
import entity.User;
import entity.builder.UserBuilder;
import enums.PaymentType;
import enums.UserRole;
import enums.UserStatus;
import settings.Patterns;
import settings.Queries;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PaymentDaoImpl extends AbstractDao implements IDao<Payment> {

    @Override
    public Payment getItemFromResultSet(ResultSet resultSet) throws SQLException {
        User user = DependencyManager.userRepo.getUserById(resultSet.getInt(2));
        return new Payment(resultSet.getInt(1), user, BigDecimal.valueOf(resultSet.getDouble(3)),
                resultSet.getDate(4), PaymentType.valueOf(resultSet.getString(5)),
                resultSet.getString(6));
    }

    @Override
    public Payment setItemId(Object item, int id) {
        Payment payment = (Payment) item;
        payment.setId(id);
        return payment;
    }

    @Override
    public PreparedStatement getGetStatement(Connection connection, long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Queries.GET_PAYMENT_BY_ID);
        statement.setLong(1, id);
        return statement;
    }

    @Override
    public PreparedStatement getListStatement(Connection connection, Map<String, String> parameters) throws SQLException {
        QueryBuilder queryBuilder = new QueryBuilder(Queries.GET_PAYMENTS_LIST, parameters);
        return connection.prepareStatement(queryBuilder.build());
    }

    @Override
    public PreparedStatement getCountStatement(Connection connection, Map<String, String> parameters) throws SQLException {
        QueryBuilder queryBuilder = new QueryBuilder(Queries.GET_PAYMENTS_COUNT, parameters);
        return connection.prepareStatement(queryBuilder.buildOnlySearch());
    }

    @Override
    public PreparedStatement getAddStatement(Connection connection, Object entity) throws SQLException {
        Payment payment = (Payment) entity;
        PreparedStatement statement = connection.prepareStatement(Queries.INSERT_PAYMENT, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, payment.getUser().getId());
        statement.setString(2, String.valueOf(payment.getValue()));
        String dateInString = new SimpleDateFormat(Patterns.datePattern).format(payment.getData());
        statement.setString(3, dateInString);
        statement.setString(4, payment.getType().toString());
        statement.setString(5, payment.getDescription());
        return statement;
    }

    @Override
    public PreparedStatement getUpdateStatement(Connection connection, Object entity) throws SQLException {
        Payment payment = (Payment) entity;
        PreparedStatement statement = connection.prepareStatement(Queries.INSERT_PAYMENT, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, payment.getUser().getId());
        statement.setString(2, String.valueOf(payment.getValue()));
        String dateInString = new SimpleDateFormat(Patterns.datePattern).format(payment.getData());
        statement.setString(3, dateInString);
        statement.setString(4, payment.getType().toString());
        statement.setString(5, payment.getDescription());
        statement.setInt(6, payment.getId());
        return statement;
    }

    @Override
    public PreparedStatement getDeleteStatement(Connection connection, long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(Queries.DELETE_PAYMENT);
        statement.setLong(1, id);
        return statement;
    }

    @Override
    public Optional<Payment> get(Connection connection, long id) throws SQLException {
        return super.getRecord(connection, id);
    }

    @Override
    public List<Payment> getList(Connection connection, Map<String, String> parameters) throws SQLException {
        return super.getRecordsList(connection, parameters);
    }

    @Override
    public int getCount(Connection connection, Map<String, String> parameters) throws SQLException {
        return super.getRecordsCount(connection, parameters);
    }

    @Override
    public Optional<Payment> add(Connection connection, Payment payment) throws SQLException {
        return super.addRecord(connection, payment);
    }

    @Override
    public void update(Connection connection, Payment payment) throws SQLException {
        super.updateRecord(connection, payment);
    }

    @Override
    public void delete(Connection connection, long id) throws SQLException {
        super.deleteRecord(connection, id);
    }
}
