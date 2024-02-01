package io.github.honoriuss.mapr.generator;

import com.squareup.javapoet.CodeBlock;
import io.github.honoriuss.mapr.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author H0n0riuss
 */
abstract class AQueryCreator {
    protected static CodeBlock createQueryStatement(String methodName, List<String> attributeList) {
        return createQueryStatement(methodName, null, attributeList);
    }

    protected static CodeBlock createQueryStatement(String methodName, Class<?> clazz, List<String> attributeList) {
        var codeBlock = CodeBlock.builder();
        methodName = cutMethodName(methodName);

        var queryParts = extractQueryParts(methodName, clazz);

        int attributesIndex = 0;
        int columnsIndex = 0;

        if (queryParts.EQueryPartsList == null || queryParts.EQueryPartsList.isEmpty()) {
            return null;
        }

        for (EQueryPart EQueryPart : queryParts.EQueryPartsList) {
            var part = new StringBuilder();
            part.append(".")
                    .append(EQueryPart.getTranslation())
                    .append("(");
            for (int i = 0; i < EQueryPart.getNumberOfArguments(); ++i, ++attributesIndex) {
                if (attributeList == null || attributeList.size() < attributesIndex + i) {
                    throw new IllegalArgumentException("ClassList cant be null if argument needs an attribute");
                }
                if (EQueryPart.hasColumnName()) {
                    if (queryParts.columnList == null || queryParts.columnList.size() < columnsIndex) {
                        throw new IllegalArgumentException("ColumnList cant be null if column is needed");
                    }
                    part.append(queryParts.columnList.get(columnsIndex++))
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
        if (methodName.contains("OrderBy")) { //TODO throw exception?
            methodName = methodName.split("OrderBy")[0];
        }
        if (methodName.contains("(")) { //TODO throw exception?
            methodName = methodName.split("\\(")[0];
        }
        var split = methodName.split("By", 2);
        if (split.length >= 2) {
            return split[1];
        }
        return split[0];
    }

    private static ColumnAttributeModel extractQueryParts(String methodName, Class<?> clazz) {
        var resList = new ColumnAttributeModel();
        List<String> classAttributes = new ArrayList<>();
        if (clazz != null) {
            var attributes = StringUtils.getAttributesFromClass(clazz); //TODO hier weiter machen
            for (var attribute : attributes) {
                var modifiedAttribute = Character.toUpperCase(attribute.charAt(0)) + attribute.substring(1);
                classAttributes.add(modifiedAttribute);
            }
        }
        while (!methodName.isEmpty()) {
            var oldMethod = methodName;
            for (String keyword : EQueryPart.ALL_KEYWORDS) {
                if (methodName.startsWith(keyword)) {
                    resList.EQueryPartsList.add(EQueryPart.valueOf(keyword.toUpperCase()));
                    methodName = methodName.substring(keyword.length());
                }
                for (String column : classAttributes) {
                    if (methodName.startsWith(column)) {
                        resList.columnList.add(column);
                        methodName = methodName.substring(column.length());
                    }
                }
            }
            if (oldMethod.equals(methodName)) {
                throw new IllegalArgumentException(String.format("Method %s or Class Attribute not supported", methodName));
            }
        }
        return resList;
    }

    private static class ColumnAttributeModel {
        public List<EQueryPart> EQueryPartsList = new ArrayList<>();
        public List<String> columnList = new ArrayList<>();
    }
}
