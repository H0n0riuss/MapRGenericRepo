package io.github.honoriuss.mapr.generator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author H0n0riuss
 */
class QueryModel {
    private final String translation;
    private final boolean hasColumnName;
    private final List<String> columnNameList = new ArrayList<>();
    private final List<String> argumentList = new ArrayList<>();
    public QueryModel(String translation, boolean hasColumnName){
        this.translation = translation;
        this.hasColumnName = hasColumnName;
    }

    public String getTranslation() {
        return translation;
    }

    public boolean getHasColumnName() {
        return hasColumnName;
    }

    public List<String> getColumnNameList() {
        return columnNameList;
    }

    public List<String> getArgumentList() {
        return argumentList;
    }

}
