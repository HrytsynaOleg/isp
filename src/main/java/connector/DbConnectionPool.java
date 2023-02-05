package connector;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import exceptions.DbConnectionException;
import java.sql.Connection;
import java.sql.SQLException;

import static settings.properties.AppPropertiesManager.getProperty;

public class DbConnectionPool {

    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource hikariDataSource;

    private DbConnectionPool() {
    }
    static {
        config.setJdbcUrl(getProperty("connection.url"));
        config.setUsername(getProperty("connection.user"));
        config.setPassword(getProperty("connection.password"));
        config.setDriverClassName(getProperty("connection.driver"));
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "100");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariDataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }

    public static Connection startTransaction () throws SQLException {
        Connection connection = hikariDataSource.getConnection();
        connection.setAutoCommit(false);
        return connection;
    }

    public static void commitTransaction(Connection connection) throws SQLException {
        connection.commit();
        connection.close();
    }

    public static void rollbackTransaction (Connection connection) throws SQLException {
        if (connection!=null) {
            connection.rollback();
            connection.close();
        }
    }
}


