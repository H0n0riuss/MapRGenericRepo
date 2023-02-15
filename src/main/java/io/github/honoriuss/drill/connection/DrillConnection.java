package io.github.honoriuss.drill.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DrillConnection {
    private static String connectionString = "jdbc:drill:zk=%s:%s/drill/%s";

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null)
            connection = DriverManager.getConnection(connectionString);
        return connection;
    }

    public DrillConnection(String node, String port, String host) {
        connectionString = String.format(connectionString, node, port, host);
    }
}
