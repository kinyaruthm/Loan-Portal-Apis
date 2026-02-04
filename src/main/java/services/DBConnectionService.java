package services;

import jakarta.enterprise.context.ApplicationScoped;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

@ApplicationScoped
public class DBConnectionService {
    private static final String URL =
            "jdbc:mysql://127.0.0.1:3306/Deploy";
    private static final String USER = "root";
    private static final String PASSWORD = "UPKFA<72-(";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
