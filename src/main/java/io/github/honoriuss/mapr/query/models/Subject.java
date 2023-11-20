package io.github.honoriuss.mapr.query.models;

import io.github.honoriuss.mapr.utils.Assert;

import java.util.regex.Pattern;

/**
 * @author H0n0riuss
 */
public class Subject {
    private static final Pattern SAVE_PREFIX = Pattern.compile("^(save|insert)");
    private static final Pattern DELETE_PREFIX = Pattern.compile("^(remove|delete)");
    private static final Pattern READ_PREFIX = Pattern.compile("^(read|find|get)");
    private static final Pattern UPDATE_PREFIX = Pattern.compile("^(update|replace)");
    private static final Pattern ALL_PREFIX = Pattern.compile("^(save|insert|remove|delete|read|find|get|update|replace)");

    public Subject(String source) {
        Assert.notNull(source, "Source cant be null");
        if (!ALL_PREFIX.matcher(source).find())
            throw new IllegalArgumentException("No Subject provided");
    }
}
