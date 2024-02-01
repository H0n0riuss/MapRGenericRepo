package io.github.honoriuss.mapr.query.parser;

import io.github.honoriuss.mapr.query.models.Query;
import io.github.honoriuss.mapr.utils.Assert;

/**
 * @author H0n0riuss
 */
@Deprecated
public class QueryCreator {

    public static Query getQuery(String interfaceMethod, Class<?> clazz) {
        return new Query(interfaceMethod, clazz);
    }

    public static String[] createCondition(String methodName) {
        Assert.hasText(methodName, "MethodName must not be null.");
        Assert.containsString(methodName, "By", "MethodName must contain 'By'.");
        return methodName.split("By")[1].split("(?=[A-Z])");
    }

    public String extractReturnType(String query) {
        return query.split(" ")[0];
    }

    public String extractSubject(String query) {
        return query.split(" ")[1].split("By")[0];
    }

    public String[] extractParts(String query) {
        //TODO split subject and other parts from query
        //put them as documents together return them as String
        return query.split("By")[1].split("(?=[A-Z])");
    }
}
