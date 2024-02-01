package io.github.honoriuss.mapr.generator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author H0n0riuss
 */
class QueryConditionModel {
    private List<QueryModel> eQueryPartList = new ArrayList<>();
    private List<QueryModel> eConditionPartList = new ArrayList<>();

    public List<QueryModel> getQueryPartList() {
        return eQueryPartList;
    }

    public List<QueryModel> getConditionPartList() {
        return eConditionPartList;
    }

}
