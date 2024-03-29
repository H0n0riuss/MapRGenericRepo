package io.github.honoriuss.mapr.generator;

import com.squareup.javapoet.CodeBlock;

import java.util.List;

/**
 * @author H0n0riuss
 */
abstract class AQueryConditionCreator {

    protected static CodeBlock createConditionCodeBlock(List<QueryModel> eConditionParts) {
        var codeBlock = CodeBlock.builder();
        for (var conditionPart : eConditionParts) {
            var part = new StringBuilder();
            part.append(".")
                    .append(conditionPart.getTranslation())
                    .append("(");
            if (conditionPart.getHasColumnName()) {
                for (var column : conditionPart.getColumnNameList()) {
                    part.append("\"")
                            .append(Character.toLowerCase(column.charAt(0)))
                            .append(column.substring(1))
                            .append("\"")
                            .append(",");
                }
            }
            for (var argument : conditionPart.getArgumentList()) {
                part.append(argument)
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

    protected static CodeBlock createQueryCodeBlock(List<QueryModel> eQueryPartList) {
        var codeBlock = CodeBlock.builder();
        for (var queryPart : eQueryPartList) { //TODO Select as special condition?
            var part = new StringBuilder();
            part.append(".")
                    .append(queryPart.getTranslation())
                    .append("(");
            if (queryPart.getHasColumnName()) {
                for (var column : queryPart.getColumnNameList()) {
                    part.append("\"")
                            .append(column)
                            .append("\"")
                            .append(",");
                }
            }
            for (var argument : queryPart.getArgumentList()) {
                part.append(argument)
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
}
