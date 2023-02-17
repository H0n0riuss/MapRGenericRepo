package io.github.honoriuss.drill.connections.models;

public class MapRConfig {
    private String[] hosts;
    private String clusterName;
    private String port;

    public MapRConfig(String[] hosts, String clusterName, String port) {
        this.hosts = hosts;
        this.clusterName = clusterName;
        this.port = port;
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

}
