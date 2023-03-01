package io.github.honoriuss.mapr.connections;

import org.ojai.store.Connection;
import org.ojai.store.DriverManager;

public abstract class OjaiConnector {
    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            connection = DriverManager.getConnection("ojai:mapr:");
        }
        return connection;
    }
}
