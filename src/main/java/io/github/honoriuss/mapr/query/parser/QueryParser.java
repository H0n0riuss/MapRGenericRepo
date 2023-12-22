package io.github.honoriuss.mapr.query.parser;

import io.github.honoriuss.mapr.query.models.Query;
import io.github.honoriuss.mapr.query.models.QueryPart;
import io.github.honoriuss.mapr.utils.Assert;

import java.util.List;
import java.util.Optional;

public abstract class QueryParser {
    public static String createMethodCalls(Query query) {
        Assert.notNull(query, "Query cant be null");
        if (query.getQueryParts().isEmpty() || query.getQueryParts().get().getQueryTypeModelList().isEmpty()) {
            return "";
        }

        var resultString = new StringBuilder();
        var queryParts = query.getQueryParts().get()
                .getQueryTypeModelList().get();
        for (var queryPart : queryParts) {
            if (queryPart.getQueryAttributes().isPresent()) {
                resultString.append(createLine(queryPart.getQueryType(), queryPart.getColumnName(), queryPart.getQueryAttributes().get()));
            } else {
                resultString.append(createLine(queryPart.getQueryType(), queryPart.getColumnName(), null));
            }
        }

        return resultString.toString();
    }

    private static String createLine(QueryPart.EQueryType eQueryType, String columnName, List<String> optAttributes) {
        var resString = new StringBuilder("." + eQueryType.getTranslation() + "(")
                .append(columnName);
        if (optAttributes != null && !optAttributes.isEmpty()) {
            resString.append(", ");
            var iterator = optAttributes.iterator();
            while (iterator.hasNext()) {
                resString.append(iterator.next());
                if (iterator.hasNext()) {
                    resString.append(",");
                }
            }
        }
        resString.append(")");
        return resString.toString();
    }
}
