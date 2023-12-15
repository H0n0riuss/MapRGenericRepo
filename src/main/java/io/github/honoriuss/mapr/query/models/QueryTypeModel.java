package io.github.honoriuss.mapr.query.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author H0n0riuss
 */
public class QueryTypeModel {
    private final QueryType.EQueryType queryType;
    private final List<String> queryAttributes = new ArrayList<>();

    public QueryTypeModel(QueryType.EQueryType queryType) {
        this.queryType = queryType;
    }

    public void addQueryAttribute(String newEntry) {
        if (queryAttributes.size() >= queryType.getNumberOfArguments()) {
            throw new IllegalArgumentException("Can`t add more attributes than number of Arguments of queryType");
        }
        queryAttributes.add(newEntry);
    }

    public QueryType.EQueryType getQueryType() {
        return queryType;
    }

    public Optional<List<String>> getQueryAttributes() {
        if (queryAttributes.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(queryAttributes);
    }
}
