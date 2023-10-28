package io.github.honoriuss.mapr.connections;

import org.ojai.store.Connection;
import org.ojai.store.DriverManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OjaiConnector {
    @Value("${mapr.ojai.url}")
    private String url;
    private Connection connection;

    public Connection getConnection() {
        if (connection == null) {
            connection = DriverManager.getConnection(url);
        }
        return connection;
    }
}
