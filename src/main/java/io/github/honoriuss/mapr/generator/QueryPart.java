package io.github.honoriuss.mapr.generator;

import java.util.*;

/**
 * @author H0n0riuss
 */
public enum QueryPart {
    LIMIT("limit", false, "Limit");

    public static final Collection<String> ALL_KEYWORDS;
    private static final List<QueryPart> ALL = List.of(LIMIT);
    private final List<String> keywords;
    private final int numberOfArguments;
    private final String translation;
    private final boolean hasColumnName;

    public int getNumberOfArguments() {
        return this.numberOfArguments;
    }

    public String getTranslation() {
        return this.translation;
    }

    public boolean hasColumnName() {
        return this.hasColumnName;
    }

    QueryPart(String translation, int numberOfArguments, boolean hasColumnName, String... keywords) {
        this.translation = translation;
        this.numberOfArguments = numberOfArguments;
        this.hasColumnName = hasColumnName;
        this.keywords = Arrays.asList(keywords);
    }

    QueryPart(String translation, boolean hasColumnName, String... keywords) {
        this(translation, 1, hasColumnName, keywords);
    }

    QueryPart(String translation, int numberOfArguments, String... keywords) {
        this(translation, numberOfArguments, true, keywords);
    }

    QueryPart(String translation, String... keywords) {
        this(translation, 1, keywords);
    }

    static {
        List<String> allKeywords = new ArrayList<>();

        for (QueryPart queryPart : ALL) {
            allKeywords.addAll(queryPart.keywords);
        }

        ALL_KEYWORDS = Collections.unmodifiableList(allKeywords);
    }
}
