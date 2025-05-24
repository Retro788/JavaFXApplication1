package javafxapplication1.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtil {
    private static HikariDataSource ds;
    static {
        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(ConfigLoader.get("jdbc.url"));
        cfg.setUsername(ConfigLoader.get("jdbc.user"));
        cfg.setPassword(ConfigLoader.get("jdbc.password"));
        cfg.setMaximumPoolSize(10);
        ds = new HikariDataSource(cfg);
    }
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}