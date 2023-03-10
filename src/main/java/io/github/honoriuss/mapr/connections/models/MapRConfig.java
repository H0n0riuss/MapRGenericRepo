package io.github.honoriuss.mapr.connections.models;

public class MapRConfig {
    private String[] hosts;
    private String clusterName;
    private String port;
    private String driver;
    private String connectionPattern;

    public MapRConfig(String[] hosts, String clusterName, String port, String driver, String connectionPattern) {
        this.hosts = hosts;
        this.clusterName = clusterName;
        this.port = port;
        this.driver = driver;
        this.connectionPattern = connectionPattern;
    }

    public MapRConfig(){
    }

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
