package io.github.honoriuss.mapr.connections;

import io.github.honoriuss.mapr.connections.models.MapRConfig;
import io.github.honoriuss.mapr.utils.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class DrillConnection {
    private static final String[] JDBC_DRIVER = {
            "org.apache.drill.jdbc.Driver",
            "com.mapr.drill.jdbc42.Driver",
            "com.mapr.drill.jdbc41.Driver"
    };
    private static final String CONNECTION_STRING_PATTERN = "jdbc:drill:zk=%s/drill/%s-drillbits;auth=maprsasl";

    private static Connection connection;

    private static String drillUrl;

    public static Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        DrillConnection.connection = connection;
    }

    public DrillConnection(MapRConfig mapRConfig) throws ClassNotFoundException, SQLException {
        this(mapRConfig.getHosts(), mapRConfig.getPort(), mapRConfig.getClusterName(), mapRConfig.getDriver(),
                mapRConfig.getConnectionPattern());
    }

    public DrillConnection() {
    }

    public DrillConnection(String[] hosts, String port, String clusterName, String driver, String connectionPattern)
            throws ClassNotFoundException, SQLException {
        var conString = buildConnectionString(hosts, port);

        setDrillUrl(connectionPattern, conString, clusterName);

        if (!StringUtils.hasText(driver)) {
            tryFindDefaultJDBCDriver();
        } else {
            Class.forName(driver);
            connection = DriverManager.getConnection(drillUrl);
        }
    }

    private String buildConnectionString(String[] hosts, String port) {
        return Arrays.stream(hosts)
                .map(host -> host.concat(":").concat(port))
                .collect(Collectors.joining(","));
    }

    private void tryFindDefaultJDBCDriver() {
        for (var driver : JDBC_DRIVER) {
            try {
                Class.forName(driver);
                connection = DriverManager.getConnection(drillUrl);
                break;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void setDrillUrl(String connectionPattern, String conString, String clusterName) {
        if (!StringUtils.hasText(connectionPattern))
            drillUrl = String.format(CONNECTION_STRING_PATTERN, conString, clusterName);
        else
            drillUrl = String.format(connectionPattern, conString, clusterName);
    }
}
