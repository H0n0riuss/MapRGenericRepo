package io.github.honoriuss.drill.connections;

import io.github.honoriuss.drill.connections.models.MapRConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DrillConnection {
    private static final String JDBC_DRIVER = "org.apache.drill.jdbc.Driver";
    private static final String CONNECTION_STRING_PATTERN = "jdbc:drill:zk=%s/drill/%s-drillbits;auth=maprsasl";
    private static Connection connection;

    private static String drillUrl;

    public static Connection getConnection() {
        return connection;
    }

    public DrillConnection(MapRConfig mapRConfig) throws ClassNotFoundException, SQLException {
        this(mapRConfig.getHosts(), mapRConfig.getPort(), mapRConfig.getClusterName());
    }

    public DrillConnection(String[] hosts, String port, String clusterName) throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        connection = DriverManager.getConnection(drillUrl);

        var conString = buildConnectionString(hosts, port);
        drillUrl = String.format(CONNECTION_STRING_PATTERN, conString, clusterName);
    }

    private String buildConnectionString(String[] hosts, String port) {
        return Arrays.stream(hosts)
                .map(host -> host.concat(":").concat(port))
                .collect(Collectors.joining(","));
    }
}
