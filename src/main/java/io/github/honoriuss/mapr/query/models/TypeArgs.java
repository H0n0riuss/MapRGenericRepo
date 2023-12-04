package io.github.honoriuss.mapr.query.models;

import io.github.honoriuss.mapr.utils.Assert;

/**
 * @author H0n0riuss
 */
public class TypeArgs {
    private String[] argName;
    private String[] argType;

    public TypeArgs(String source) {
        Assert.notNull(source, "Source can`t be null");
        extractArgs(source);
    }

    private void extractArgs(String source) {
        this.argName = new String[3];
    }

}
