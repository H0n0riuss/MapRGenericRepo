package io.github.honoriuss.mapr.connections;

import io.github.honoriuss.mapr.connections.models.MapRConfig;
import io.github.honoriuss.mapr.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class DrillConnection {
    private final MapRConfig mapRConfig;
    private Connection connection;
    private String drillUrl;

    public Connection getConnection() {
        return connection;
    }

    public DrillConnection(MapRConfig mapRConfig) {
        this.mapRConfig = mapRConfig;
        setDrillUrl(mapRConfig.getConnectionPattern(), buildConnectionString());

        initJDBCDriver();
    }

    private String buildConnectionString() {
        return Arrays.stream(mapRConfig.getHosts())
                .map(host -> host.concat(":").concat(mapRConfig.getPort()))
                .collect(Collectors.joining(","));
    }

    private void initJDBCDriver() {
        try {
            Class.forName(mapRConfig.getDriver());
            connection = DriverManager.getConnection(drillUrl);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void setDrillUrl(String connectionPattern, String conString) {
        if (!StringUtils.hasText(connectionPattern))
            drillUrl = String.format(mapRConfig.getConnectionPattern(), conString, mapRConfig.getClusterName());
        else
            drillUrl = String.format(connectionPattern, conString, mapRConfig.getClusterName());
    }
}
