package io.github.honoriuss.mapr.connections;

import org.ojai.store.Connection;
import org.ojai.store.DriverManager;

public abstract class OjaiConnector {
    private String url;
    private Connection connection;

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
