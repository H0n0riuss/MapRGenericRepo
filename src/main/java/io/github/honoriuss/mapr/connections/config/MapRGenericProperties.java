package io.github.honoriuss.mapr.connections.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author H0n0riuss
 */
@Component
@ConfigurationProperties(prefix = "mapr.drill.connection.jdbc")
public class MapRGenericProperties {
    private String hosts;
    private String clusterName;
    private int port;
    private String driver;
    private String connection_pattern;

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getConnection_pattern() {
        return connection_pattern;
    }

    public void setConnection_pattern(String connection_pattern) {
        this.connection_pattern = connection_pattern;
    }
}
