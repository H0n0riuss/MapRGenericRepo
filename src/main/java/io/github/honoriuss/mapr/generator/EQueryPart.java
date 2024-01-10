package io.github.honoriuss.mapr.generator;

import java.util.*;

/**
 * @author H0n0riuss
 */
public enum EQueryPart {
    SELECT("select", false, "Select"),
    ORDERBY("orderBy", "OrderBy"),
    OFFSET("offset", "Offset"),
    LIMIT("limit", false, "Limit");

    public static final Collection<String> ALL_KEYWORDS;
    private static final List<EQueryPart> ALL = List.of(SELECT, ORDERBY, OFFSET, LIMIT);
    private final List<String> keywords;
    private final int numberOfArguments;
    private final String translation;
    private final boolean hasColumnName;
    public List<String> columnNameList = new ArrayList<>();
    public List<String> argumentList = new ArrayList<>();

    public int getNumberOfArguments() {
        return this.numberOfArguments;
    }

    public String getTranslation() {
        return this.translation;
    }

    public boolean hasColumnName() {
        return this.hasColumnName;
    }

    EQueryPart(String translation, int numberOfArguments, boolean hasColumnName, String... keywords) {
        this.translation = translation;
        this.numberOfArguments = numberOfArguments;
        this.hasColumnName = hasColumnName;
        this.keywords = Arrays.asList(keywords);
    }

    EQueryPart(String translation, boolean hasColumnName, String... keywords) {
        this(translation, 1, hasColumnName, keywords);
    }

    EQueryPart(String translation, int numberOfArguments, String... keywords) {
        this(translation, numberOfArguments, true, keywords);
    }

    EQueryPart(String translation, String... keywords) {
        this(translation, 1, keywords);
    }

    static {
        List<String> allKeywords = new ArrayList<>();

        for (EQueryPart eQueryPart : ALL) {
            allKeywords.addAll(eQueryPart.keywords);
        }

        ALL_KEYWORDS = Collections.unmodifiableList(allKeywords);
    }
}
