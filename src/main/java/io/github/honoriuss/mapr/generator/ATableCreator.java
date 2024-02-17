package io.github.honoriuss.mapr.generator;

/**
 * @author H0n0riuss
 */
abstract class ATableCreator {
    public static String createTable(boolean viaRest){
        if (viaRest) {
            return """
                if (!tableCreator.tableExists()) {
                   tableCreator.createTable(this.tablePath);
                }""";
        }
        return "";
    }
}
