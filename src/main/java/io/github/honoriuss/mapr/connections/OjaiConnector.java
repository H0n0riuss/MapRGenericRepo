package io.github.honoriuss.mapr.connections;

import org.ojai.store.Connection;
import org.ojai.store.DriverManager;
import org.springframework.beans.factory.annotation.Value;

public abstract class OjaiConnector {
    @Value("${mapr.ojai.url}")
    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            connection = DriverManager.getConnection("ojai:mapr:");
        }
        return connection;
    }
}
