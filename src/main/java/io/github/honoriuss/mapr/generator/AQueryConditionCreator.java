package io.github.honoriuss.mapr.generator;

import com.squareup.javapoet.CodeBlock;

import java.util.List;

/**
 * @author H0n0riuss
 */
public abstract class AQueryConditionCreator {

    public static CodeBlock createConditionCodeBlock(List<EConditionPart> eConditionParts) {
        var codeBlock = CodeBlock.builder();
        for (var conditionPart : eConditionParts) {
            var part = new StringBuilder();
            part.append(".")
                    .append(conditionPart.getTranslation())
                    .append("(");
            if (conditionPart.hasColumnName()) {
                for (var column : conditionPart.columnNameList) {
                    part.append("\"")
                            .append(column)
                            .append("\"")
                            .append(",");
                }
            }
            for (var argument : conditionPart.argumentList) {
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

    public static CodeBlock createQueryCodeBlock(List<EQueryPart> eQueryPartList) {
        var codeBlock = CodeBlock.builder();
        for (var queryPart : eQueryPartList) { //TODO Select as special condition?
            var part = new StringBuilder();
            part.append(".")
                    .append(queryPart.getTranslation())
                    .append("(");
            if (queryPart.hasColumnName()) {
                for (var column : queryPart.columnNameList) {
                    part.append("\"")
                            .append(column)
                            .append("\"")
                            .append(",");
                }
            }
            for (var argument : queryPart.argumentList) {
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
