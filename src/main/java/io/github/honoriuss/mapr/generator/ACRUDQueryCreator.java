package io.github.honoriuss.mapr.generator;

import java.util.ArrayList;

/**
 * @author H0n0riuss
 */
abstract class ACRUDQueryCreator { //TODO everything with many

    protected static String getCreateString(ArrayList<String> argumentStringList, boolean hasReturnType) {//TODO List
        var arg0 = argumentStringList.get(0);
        var res = arg0 + ".set_id(java.util.UUID.randomUUID().toString());\n";
        res += String.format("org.ojai.Document newDoc = connection.newDocument(%s);\n", arg0);
        res += "store.insert(newDoc)";
        if (!hasReturnType) {
            res += String.format("; \nreturn %s", arg0);
        }
        return res;
    }

    protected static String getReadString(String entityClassName, String query, boolean hasListReturnType) {
        var res = query;
        if (!query.isEmpty()) {
            res += "var queryResult = store.find(query);\n";
        } else {
            res += "var queryResult = store.find();\n";
        }

        if (hasListReturnType) {
            res += String.format("var resList = new java.util.ArrayList<%s>();\n", entityClassName);
            res += String.format(
                    """
                            for (var entry : queryResult) {
                               resList.add(entry.toJavaBean(%s.class));
                            }
                            """, entityClassName);
            res += "return resList";
        } else {
            res += String.format("return queryResult.iterator().next().toJavaBean(%s.class)", entityClassName);
        }
        return res;
    }

    protected static String getUpdateString(ArrayList<String> argumentStringList) {
        var arg0 = argumentStringList.get(0);
        var resString = String.format("org.ojai.Document doc = connection.newDocument(%s);\n", arg0);
        resString += "store.replace(doc);\n";
        resString += String.format("return %s", arg0);
        return resString;
    }

    protected static String getDeleteString(ArrayList<String> argumentStringList) { //TODO .checkAndDelete impl for query
        var arg0 = argumentStringList.get(0);
        return String.format("store.delete(%s)", arg0);
    }
}
