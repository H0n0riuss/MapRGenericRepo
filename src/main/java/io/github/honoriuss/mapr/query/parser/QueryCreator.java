package io.github.honoriuss.mapr.query.parser;

import io.github.honoriuss.mapr.utils.Assert;

/**
 * @author H0n0riuss
 */
public class QueryCreator {

    private PartTree tree;

    public static String[] createCondition(String methodName) {
        Assert.hasText(methodName, "MethodName must not be null");
        return methodName.split("By")[1].split("(?=[A-Z])");
    }

    private String extractParts(String inputQuery){
        //TODO split subject and other parts from query
        //put them as documents together return them as String
        return "";
    }
}
