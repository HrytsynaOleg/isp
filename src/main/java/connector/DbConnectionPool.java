package connector;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import settings.Settings;
import java.io.FileReader;
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
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariDataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }

    static class DbConnectionProperties {
        private static final Properties properties = new Properties();
        static {
            try {
                properties.load(new FileReader(Settings.DB_CONNECTION_PROP));
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
    }
}


