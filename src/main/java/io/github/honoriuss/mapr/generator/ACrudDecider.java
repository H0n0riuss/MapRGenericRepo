package io.github.honoriuss.mapr.generator;

/**
 * @author H0n0riuss
 */
abstract class ACrudDecider {
    protected static String getCrudTranslation(String methodName) {
        methodName = methodName.split("[A-Z]")[0];
        return ECrudType.fromProperty(methodName);
    }

    protected static ECrudType getCrudType(String methodName) {
        methodName = methodName.split("[A-Z]")[0];
        return ECrudType.getCrudTypeFromProperty(methodName);
    }
}
