package io.github.honoriuss.mapr.connections.connectors;

import io.github.honoriuss.mapr.connections.interfaces.IOjaiConnector;
import org.ojai.store.Connection;
import org.ojai.store.DriverManager;

public class OjaiConnector implements IOjaiConnector {
    private String url = "ojai:mapr:";
    private Connection connection;

    @Override
    public Connection getConnection() {
        if (connection == null) {
            connection = DriverManager.getConnection(url);
        }
        return connection;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
