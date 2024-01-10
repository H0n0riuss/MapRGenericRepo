package io.github.honoriuss.mapr.generator;

import io.github.honoriuss.mapr.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author H0n0riuss
 */
public abstract class AQueryConditionExtractor {
    public static QueryConditionModel extractQueryCondition(String methodName, List<String> argumentList, Class<?> clazz) {
        var queryResult = new QueryConditionModel();
        List<String> columnsList = new ArrayList<>();
        if (clazz != null) {
            var attributes = StringUtils.getAttributesFromClass(clazz); //TODO hier weiter machen
            for (var attribute : attributes) {
                var modifiedAttribute = Character.toUpperCase(attribute.charAt(0)) + attribute.substring(1);
                columnsList.add(modifiedAttribute);
            }
        }
        var argumentIndex = 0;
        while (!methodName.isEmpty()) {
            var oldMethod = methodName;
            for (String keyword : EQueryPart.ALL_KEYWORDS) {
                if (methodName.startsWith(keyword)) {
                    var key = EQueryPart.valueOf(keyword.toUpperCase());
                    methodName = methodName.substring(keyword.length());
                    if (key.hasColumnName()) {
                        for (var column : columnsList) {
                            if (methodName.startsWith(column)) {
                                key.columnName.add(column);
                                methodName = methodName.substring(column.length());
                                break;
                            }
                        }
                    }
                    for (int i = 0; i < key.getNumberOfArguments(); ++i) {
                        key.argumentList.add(argumentList.get(i + argumentIndex));
                    }
                    argumentIndex += key.argumentList.size();
                    queryResult.eQueryPartList.add(key);
                }
            }
            for (String keyword : EConditionPart.ALL_KEYWORDS) {
                if (methodName.startsWith(keyword)) {
                    var key = EConditionPart.valueOf(keyword.toUpperCase());
                    methodName = methodName.substring(keyword.length());
                    if (key.hasColumnName()) {
                        for (var column : columnsList) {
                            if (methodName.startsWith(column)) {
                                key.columnName.add(column);
                                methodName = methodName.substring(column.length());
                                break;
                            }
                        }
                    }
                    for (int i = 0; i < key.getNumberOfArguments(); ++i) {
                        key.argumentList.add(argumentList.get(i + argumentIndex));
                    }
                    argumentIndex += key.argumentList.size();
                    queryResult.eConditionPartList.add(key);
                }
            }
            if (oldMethod.equals(methodName)) {
                throw new IllegalArgumentException("Method or Class Attribute not supported");
            }
        }
        return queryResult;
    }
}
