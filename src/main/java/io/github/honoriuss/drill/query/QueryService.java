package io.github.honoriuss.drill.query;

import io.github.honoriuss.drill.connections.DrillConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryService {
    public ResultSet ExecuteQuery(String query) throws SQLException {
        Statement st = DrillConnection.getConnection().createStatement();
        return st.executeQuery(query);
    }

    public DrillResult getQueryResult(String query) throws SQLException {
        DrillResult result = null;
        var distinctQueries = query.split(";");
        try (Connection drillConnection = DrillConnection.getConnection()) {
            for (var distinctQuery : distinctQueries) {
                try (var statement = drillConnection.createStatement()) {
                    try (var resultSet = statement.executeQuery(distinctQuery)) {
                        result = new DrillResult(resultSet);
                    }
                }
            }
        }
        return result;
    }

    public List<Map<String, Object>> getQueryResultJson(String query) throws SQLException {
        List<Map<String, Object>> result = null;
        var distinctQueries = query.split(";");
        try (Connection drillConnection = DrillConnection.getConnection()) {
            for (var distinctQuery : distinctQueries) {
                try (var statement = drillConnection.createStatement()) {
                    try (var resultSet = statement.executeQuery(distinctQuery)) {
                        result = new ArrayList<>();
                        var count = resultSet.getMetaData().getColumnCount();
                        while (resultSet.next()) {
                            Map<String, Object> json = new HashMap<>();
                            for (int i = 1; i < count + 1; ++i) {
                                var columnName = resultSet.getMetaData().getColumnName(i);
                                Object value = resultSet.getObject(i);
                                json.put(columnName, value);
                            }
                            result.add(json);
                        }
                    }
                }
            }
        }
        return result;
    }
}
