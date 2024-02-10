package io.github.honoriuss.mapr.connections.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author H0n0riuss
 */
public class TableColumnsModel {
    @JsonProperty("name")
    private String columnName;
    @JsonProperty("type")
    private String columnType;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }
}
