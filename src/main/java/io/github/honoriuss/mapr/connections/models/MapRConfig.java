package io.github.honoriuss.mapr.connections.models;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapRConfig {
    @Value("${mapr.drill.connection.jdbc.hosts}")
    private String[] hosts;
    @Value("${mapr.drill.connection.jdbc.clusterName}")
    private String clusterName;
    @Value("${mapr.drill.connection.jdbc.port}")
    private String port;
    @Value("${mapr.drill.connection.jdbc.driver}")
    private String driver;
    @Value("${mapr.drill.connection.jdbc.connection_pattern}")
    private String connectionPattern;

    public String[] getHosts() {
        return hosts;
    }

    public void setHosts(String[] hosts) {
        this.hosts = hosts;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getConnectionPattern() {
        return connectionPattern;
    }

    public void setConnectionPattern(String connectionPattern) {
        this.connectionPattern = connectionPattern;
    }

}
