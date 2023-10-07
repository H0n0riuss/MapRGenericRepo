package io.github.honoriuss.mapr.query.parser;

import io.github.honoriuss.mapr.utils.Assert;

/**
 * @author H0n0riuss
 */
public class QueryCreator {

    private PartTree tree;

    public String createQuery(String inputQuery) {
        Assert.hasText(inputQuery, "InputQuery must not be null");
        var query = "";

        return query;
    }

    private String extractParts(String inputQuery){
        //TODO split subject and other parts from query
        //put them as documents together return them as String
        return "";
    }
}
