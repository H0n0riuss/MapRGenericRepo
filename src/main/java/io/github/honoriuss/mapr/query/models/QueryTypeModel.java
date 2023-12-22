package io.github.honoriuss.mapr.query.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author H0n0riuss
 */
public class QueryTypeModel {
    private final QueryPart.EQueryType queryType;
    private final String columnName;
    private final List<String> queryAttributes = new ArrayList<>();

    public QueryTypeModel(QueryPart.EQueryType queryType, String columnName) {
        this.queryType = queryType;
        this.columnName = columnName;
    }

    public void addQueryAttribute(String newEntry) {
        if (queryAttributes.size() >= queryType.getNumberOfArguments()) {
            throw new IllegalArgumentException("Can`t add more attributes than number of Arguments of queryType");
        }
        queryAttributes.add(newEntry);
    }

    public QueryPart.EQueryType getQueryType() {
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
