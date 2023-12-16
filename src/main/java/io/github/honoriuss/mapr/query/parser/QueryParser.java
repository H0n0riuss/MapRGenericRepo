package io.github.honoriuss.mapr.query.parser;

import io.github.honoriuss.mapr.query.models.Query;

public abstract class QueryParser {
    public static String createMethodCalls(Query query) {
        return ".like(attr1, attr2).limit(limit);";
    }
}
