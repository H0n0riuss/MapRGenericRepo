package io.github.honoriuss.mapr.generator;

import com.squareup.javapoet.CodeBlock;
import io.github.honoriuss.mapr.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author H0n0riuss
 */
public abstract class StatementCreator {
    public static CodeBlock createStatement(String methodName, List<String> attributeList) {
        return createStatement(methodName, null, attributeList);
    }

    public static CodeBlock createStatement(String methodName, Class<?> clazz, List<String> attributeList) {
        var codeBlock = CodeBlock.builder();
        methodName = cutMethodName(methodName);

        var queryParts = extractQueryParts(methodName, clazz);
        var columnList = queryParts.columnList;

        int attributesIndex = 0;
        int columnsIndex = 0;

        if (queryParts.queryPartsList == null|| queryParts.queryPartsList.isEmpty()) {
            return null;
        }

        for (QueryPart queryPart : queryParts.queryPartsList) {
            var part = new StringBuilder();
            part.append(".")
                    .append(queryPart.getTranslation())
                    .append("(");
            for (int i = 0; i < queryPart.getNumberOfArguments(); ++i, ++attributesIndex) {
                if (attributeList == null || attributeList.size() < attributesIndex + i) {
                    throw new IllegalArgumentException("ClassList cant be null if argument needs an attribute");
                }
                if (queryPart.hasColumnName()) {
                    if (columnList == null || columnList.size() < columnsIndex) {
                        throw new IllegalArgumentException("ColumnList cant be null if column is needed");
                    }
                    part.append(columnList.get(columnsIndex++))
                            .append(", ");

                }
                part.append(attributeList.get(attributesIndex + i))
                        .append(",");
            }
            var lastIndex = part.length() - 1;
            if (part.charAt(lastIndex) == ',') {
                part.deleteCharAt(lastIndex);
            }
            part.append(")");
            codeBlock.add(part.toString());
        }

        return codeBlock.build();
    }

    private static String cutMethodName(String methodName) {
        return methodName.split("By", 2)[1];
    }

    private static ColumnAttributeModel extractQueryParts(String methodName, Class<?> clazz) {
        var resList = new ColumnAttributeModel();
        List<String> classAttributes = new ArrayList<>();
        if (clazz != null) {
            classAttributes = StringUtils.getAttributesFromClass(clazz);
        }
        for (String keyword : QueryPart.ALL_KEYWORDS) {
            if (methodName.startsWith(keyword)) {
                resList.queryPartsList.add(QueryPart.valueOf(keyword.toUpperCase()));
                methodName = methodName.substring(keyword.length());
            }
            if (classAttributes != null) {
                for (String column : classAttributes) {
                    if (methodName.startsWith(column)) {
                        resList.columnList.add(column);
                        methodName = methodName.substring(column.length());
                    }
                }
            }
        }
        return resList;
    }

    private static class ColumnAttributeModel {
        public List<QueryPart> queryPartsList = new ArrayList<>();
        public List<String> columnList = new ArrayList<>();
    }
}
