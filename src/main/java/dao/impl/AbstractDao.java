package dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractDao {

    public abstract <T> T getItemFromResultSet(ResultSet resultSet) throws SQLException;

    public abstract <T> T setItemId(T item, int id);

    public abstract PreparedStatement getGetStatement(Connection connection, long id) throws SQLException;

    public abstract PreparedStatement getListStatement(Connection connection, Map<String, String> parameters) throws SQLException;

    public abstract PreparedStatement getCountStatement(Connection connection, Map<String, String> parameters) throws SQLException;

    public abstract <T> PreparedStatement getAddStatement(Connection connection, T entity) throws SQLException;

    public abstract <T> PreparedStatement getUpdateStatement(Connection connection, T entity) throws SQLException;

    public abstract PreparedStatement getDeleteStatement(Connection connection, long id) throws SQLException;


    public <T> Optional<T> getRecord(Connection connection, long id) throws SQLException {

        PreparedStatement statement = getGetStatement(connection, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return Optional.of(getItemFromResultSet(resultSet));
        }
        return Optional.empty();
    }

    public <T> List<T> getRecordsList(Connection connection, Map<String, String> parameters) throws SQLException {
        List<T> list = new ArrayList<>();
        PreparedStatement statement = getListStatement(connection, parameters);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            T item = getItemFromResultSet(resultSet);
            list.add(item);
        }
        return list;
    }

    public int getRecordsCount(Connection connection, Map<String, String> parameters) throws SQLException {
        PreparedStatement statement = getCountStatement(connection, parameters);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public <T> Optional<T> addRecord(Connection connection, T item) throws SQLException {

        PreparedStatement statement = getAddStatement(connection, item);
        statement.executeUpdate();
        ResultSet keys = statement.getGeneratedKeys();
        if (keys.next()) {
            item = setItemId(item, keys.getInt(1));
            return Optional.of(item);
        }
        return Optional.empty();

    }

    public <T> void updateRecord(Connection connection, T item) throws SQLException {

        PreparedStatement statement = getUpdateStatement(connection, item);
        statement.executeUpdate();
    }


    public void deleteRecord(Connection connection, long id) throws SQLException {
        PreparedStatement statement = getDeleteStatement(connection, id);
        statement.executeUpdate();
    }
}
