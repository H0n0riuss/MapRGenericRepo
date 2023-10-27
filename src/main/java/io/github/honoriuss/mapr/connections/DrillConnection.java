package io.github.honoriuss.mapr.connections;

import io.github.honoriuss.mapr.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class DrillConnection {
    @Value("${mapr.drill.connection.jdbc.driver}")
    private String JDBC_DRIVER;
    @Value("${mapr.drill.connection.jdbc.connection_pattern}")
    private String CONNECTION_STRING_PATTERN;
    @Value("${mapr.drill.connection.jdbc.hosts}")
    private String[] hosts;
    @Value("${mapr.drill.connection.jdbc.port}")
    private String port;
    @Value("${mapr.drill.connection.jdbc.clusterName}")
    private String clusterName;
    private Connection connection;
    private String drillUrl;

    public Connection getConnection() {
        return connection;
    }

    public DrillConnection() {
        setDrillUrl(CONNECTION_STRING_PATTERN, buildConnectionString(), clusterName);

        initJDBCDriver();
    }

    private String buildConnectionString() {
        return Arrays.stream(hosts)
                .map(host -> host.concat(":").concat(port))
                .collect(Collectors.joining(","));
    }

    private void initJDBCDriver() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(drillUrl);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void setDrillUrl(String connectionPattern, String conString, String clusterName) {
        if (!StringUtils.hasText(connectionPattern))
            drillUrl = String.format(CONNECTION_STRING_PATTERN, conString, clusterName);
        else
            drillUrl = String.format(connectionPattern, conString, clusterName);
    }
}
