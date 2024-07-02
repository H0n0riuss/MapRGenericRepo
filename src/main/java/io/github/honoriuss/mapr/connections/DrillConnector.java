package io.github.honoriuss.mapr.connections;

import io.github.honoriuss.mapr.connections.models.MapRConfig;
import io.github.honoriuss.mapr.connections.query.DrillResult;
import io.github.honoriuss.mapr.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DrillConnector { //TODO build beans with factory
    private final MapRConfig mapRConfig;
    private Connection connection;
    private String drillUrl;

    public Connection getConnection() {
        return connection;
    }

    public DrillConnector(MapRConfig mapRConfig) {
        this.mapRConfig = mapRConfig;
        setDrillUrl(mapRConfig.getConnectionPattern(), buildConnectionString());

        initJDBCDriver();
    }

    public DrillResult getQueryResult(String query) throws SQLException {
        DrillResult result;
        try (Statement statement = getConnection().createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(query)) {
                result = new DrillResult(resultSet);
            }
        }
        return result;
    }

    public List<Map<String, Object>> getQueryResultAsJson(String query) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        String[] distinctQueries = query.split(";");
        for (var distinctQuery : distinctQueries) {
            try (Statement statement = getConnection().createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(distinctQuery)) {
                    var count = resultSet.getMetaData().getColumnCount();
                    while (resultSet.next()) {
                        var json = new HashMap<String, Object>();
                        for (var i = 1; i < count; ++i) {
                            var columnName = resultSet.getMetaData().getColumnName(i);
                            var value = resultSet.getObject(i);
                            json.put(columnName, value);
                        }
                        result.add(json);
                    }
                }
            }
        }
        return result;
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
