package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IDao<T> {
    Optional<T> get(Connection connection, long id) throws SQLException;

    List<T> getList(Connection connection, Map<String, String> parameters) throws SQLException;

    int getCount(Connection connection, Map<String, String> parameters) throws SQLException;

    Optional<T> add(Connection connection, T t) throws SQLException;

    void update(Connection connection, T t) throws SQLException;

    void delete(Connection connection, long id) throws SQLException;
}
