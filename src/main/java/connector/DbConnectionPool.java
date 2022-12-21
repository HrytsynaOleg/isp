package connector;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import exeptions.DbConnectionExeption;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnectionPool {

    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource hikariDataSource;

    private DbConnectionPool() {
    }
    static {
        config.setJdbcUrl(DbConnectionProperties.getUrlFromProperties());
        config.setUsername(DbConnectionProperties.getUserFromProperties());
        config.setPassword(DbConnectionProperties.getPasswordFromProperties());
        config.setDriverClassName(DbConnectionProperties.getDriverFromProperties());
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "100");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariDataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }

    public static void startTransaction (Connection connection) throws DbConnectionExeption {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DbConnectionExeption("Unable set autocommit", e);
        }
    }

    public static void commitTransaction (Connection connection) throws DbConnectionExeption {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new DbConnectionExeption("Unable commit transaction", e);
        }
    }

    public static void rollbackTransaction (Connection connection) throws DbConnectionExeption {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new DbConnectionExeption("Unable rollback transaction", e);
        }
    }

    static class DbConnectionProperties {
        private static final Properties properties = new Properties();
        static {
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                properties.load(classLoader.getResourceAsStream("db.properties"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private static String getUrlFromProperties() {
            return properties.getProperty("connection.url");
        }

        private static String getUserFromProperties() {
            return properties.getProperty("connection.user");
        }

        private static String getPasswordFromProperties() {
            return properties.getProperty("connection.password");
        }
        private static String getDriverFromProperties() {
            return properties.getProperty("connection.driver");
        }
    }
}


