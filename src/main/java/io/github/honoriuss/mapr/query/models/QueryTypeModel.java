package io.github.honoriuss.mapr.query.models;

import io.github.honoriuss.mapr.query.enums.EQueryType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author H0n0riuss
 */
@Deprecated
public class QueryTypeModel {
    private final EQueryType queryType;
    private final String columnName;
    private final List<String> queryAttributes = new ArrayList<>();

    public QueryTypeModel(EQueryType queryType, String columnName) {
        this.queryType = queryType;
        if (this.queryType.hasColumnName()) {
            this.columnName = columnName;
        } else {
            this.columnName = null;
        }
    }

    public void addQueryAttribute(String newEntry) {
        if (queryAttributes.size() >= queryType.getNumberOfArguments()) {
            throw new IllegalArgumentException("Can`t add more attributes than number of Arguments of queryType");
        }
        queryAttributes.add(newEntry);
    }

    public EQueryType getQueryType() {
        return queryType;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public Optional<List<String>> getQueryAttributes() {
        if (queryAttributes.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(queryAttributes);
    }
}
