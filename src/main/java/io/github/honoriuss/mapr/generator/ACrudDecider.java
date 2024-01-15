package io.github.honoriuss.mapr.generator;

/**
 * @author H0n0riuss
 */
public abstract class ACrudDecider {
    public static String getCrudTranslation(String methodName) {
        methodName = methodName.split("[A-Z]")[0];
        return ECrudType.fromProperty(methodName);
    }

    public static ECrudType getCrudType(String methodName) {
        methodName = methodName.split("[A-Z]")[0];
        return ECrudType.getCrudTypeFromProperty(methodName);
    }
}
