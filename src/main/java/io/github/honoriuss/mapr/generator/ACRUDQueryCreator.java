package io.github.honoriuss.mapr.generator;

import java.util.ArrayList;

/**
 * @author H0n0riuss
 */
public abstract class ACRUDQueryCreator { //TODO everything with many

    public static String getCreateString(ArrayList<String> argumentStringList, boolean isVoidMethod) {
        var arg0 = argumentStringList.get(0);
        var res = arg0 + ".set_id(java.util.UUID.randomUUID().toString());\n";
        res += String.format("org.ojai.Document newDoc = connection.newDocument(%s);\n", arg0);
        res += "store.insert(newDoc);\n";
        if (!isVoidMethod) {
            res += String.format("return %s", arg0);
        }
        return res;
    }

    public static String getReadString(String entityClassName, String query, boolean hasListReturnType) {
        var res = query;
        res += "var queryResult = store.find(query);\n";
        res += String.format("var resList = new java.util.ArrayList<%s>();\n", entityClassName);
        res += String.format("queryResult.forEach(entry -> resList.add(entry.toJavaBean(%s.class)));\n", entityClassName);
        if (hasListReturnType) {
            res += "return resList;";
        } else {
            res += "return resList.get(0);";
        }
        return res;
    }

    public static String getUpdateString(ArrayList<String> argumentStringList) {
        var arg0 = argumentStringList.get(0);
        var resString = String.format("org.ojai.Document doc = connection.newDocument(%s);\n", arg0);
        resString += "store.replace(doc);\n";
        resString += String.format("return %s;", arg0);
        return resString;
    }

    public static String getDeleteString(ArrayList<String> argumentStringList) { //TODO .checkAndDelete impl for query
        var arg0 = argumentStringList.get(0);
        return String.format("store.delete(%s);", arg0);
    }
}
