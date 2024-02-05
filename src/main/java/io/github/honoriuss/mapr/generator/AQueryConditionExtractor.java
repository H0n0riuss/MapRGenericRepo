package io.github.honoriuss.mapr.generator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author H0n0riuss
 */
abstract class AQueryConditionExtractor {
    protected static QueryConditionModel extractQueryCondition(String methodName, List<String> argumentList, List<String> attributeList) {
        var queryResult = new QueryConditionModel();
        if (methodName.isEmpty()) {
            return queryResult;
        }
        List<String> columnsList = new ArrayList<>();
        if (attributeList != null) {
            for (var attribute : attributeList) {
                var modifiedAttribute = Character.toUpperCase(attribute.charAt(0)) + attribute.substring(1);
                columnsList.add(modifiedAttribute);
            }
        }
        if (methodName.contains("By") && !methodName.contains("OrderBy")) {
            var split = methodName.split("By", 2);
            if (split.length >= 2) {
                methodName = split[1];
            } else {
                methodName = split[0];
            }
        }
        while (!methodName.isEmpty()) {
            var oldMethod = methodName;
            for (String keyword : EQueryPart.ALL_KEYWORDS) {
                if (methodName.startsWith(keyword)) {
                    var key = EQueryPart.valueOf(keyword.toUpperCase());
                    var model = new QueryModel(key.getTranslation(), key.hasColumnName());
                    methodName = methodName.substring(keyword.length());
                    if (key.hasColumnName()) {
                        for (var column : columnsList) {
                            if (methodName.startsWith(column)) {
                                model.getColumnNameList().add(column);
                                methodName = methodName.substring(column.length());
                                break;
                            }
                        }
                    }
                    for (int i = 0; i < key.getNumberOfArguments(); ++i) {
                        model.getArgumentList().add(argumentList.remove(0));
                    }
                    queryResult.getQueryPartList().add(model);
                }
            }
            for (String keyword : EConditionPart.ALL_KEYWORDS) {
                if (methodName.startsWith(keyword)) {
                    var key = EConditionPart.valueOf(keyword.toUpperCase());
                    var model = new QueryModel(key.getTranslation(), key.hasColumnName());
                    methodName = methodName.substring(keyword.length());
                    if (key.hasColumnName()) {
                        for (var column : columnsList) {
                            if (methodName.startsWith(column)) {
                                model.getColumnNameList().add(column);
                                methodName = methodName.substring(column.length());
                            }
                        }
                    }
                    for (int i = 0; i < key.getNumberOfArguments(); ++i) {
                        model.getArgumentList().add(argumentList.remove(0));
                    }
                    queryResult.getConditionPartList().add(model);
                }
            }
            if (oldMethod.equals(methodName)) {
                throw new IllegalArgumentException("Method or Class Attribute not supported: " + methodName);
            }
        }
        return queryResult;
    }
}
