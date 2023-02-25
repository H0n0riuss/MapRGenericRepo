package io.github.honoriuss.mapr.connections.query;

import io.github.honoriuss.mapr.utils.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DrillResult {
    private final String[] columns;
    private final List<List<Object>> rows;

    public DrillResult(ResultSet resultSet) throws SQLException {
        Assert.notNull(resultSet, "ResultSet must not be null");

        this.columns = getColumns(resultSet);
        this.rows = getRows(resultSet);
    }

    public String[] getColumns() {
        return columns;
    }

    public List<List<Object>> getRows() {
        return rows;
    }

    private String[] getColumns(ResultSet resultSet) throws SQLException {
        var metaData = resultSet.getMetaData();
        var count = metaData.getColumnCount();
        return IntStream.range(1, count + 1).mapToObj(idx -> {
            try {
                return metaData.getColumnName(idx);
            } catch (SQLException ex) {
                ex.printStackTrace();
                return null;
            }

        }).toArray(String[]::new);
    }

    private List<List<Object>> getRows(ResultSet resultSet) throws SQLException {
        var rows = new ArrayList<List<Object>>();
        var count = resultSet.getMetaData().getColumnCount();
        while (resultSet.next()) {
            var row = IntStream.range(1, count + 1).mapToObj(idx -> {
                try {
                    return resultSet.getObject(idx);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    return null;
                }
            }).collect(Collectors.toList());
            rows.add(row);
        }
        return rows;
    }
}
