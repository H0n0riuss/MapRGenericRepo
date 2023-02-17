package io.github.honoriuss.mapr.connections;

import org.ojai.store.Connection;
import org.ojai.store.DriverManager;

public class OjaiConnector {
    private Connection connection;

    public Connection getConnection() {
        if (connection == null) {
            connection = DriverManager.getConnection("ojai:mapr:");
        }
        return connection;
    }
}
