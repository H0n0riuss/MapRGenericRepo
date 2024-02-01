package io.github.honoriuss.mapr.generator;

import java.util.*;

/**
 * @author H0n0riuss
 */
enum EConditionPart {
    LIKE("like", "Like", "IsLike"),
    NOTLIKE("notLike", "NotLike", "IsNotLike"),
    AND("and", false, "And"),
    OR("or", false, "Or");

    public static final Collection<String> ALL_KEYWORDS;
    private static final List<EConditionPart> ALL = List.of(LIKE, NOTLIKE, AND, OR);
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

    EConditionPart(String translation, int numberOfArguments, boolean hasColumnName, String... keywords) {
        this.translation = translation;
        this.numberOfArguments = numberOfArguments;
        this.hasColumnName = hasColumnName;
        this.keywords = Arrays.asList(keywords);
    }

    EConditionPart(String translation, boolean hasColumnName, String... keywords) {
        this(translation, 1, hasColumnName, keywords);
    }

    EConditionPart(String translation, int numberOfArguments, String... keywords) {
        this(translation, numberOfArguments, true, keywords);
    }

    EConditionPart(String translation, String... keywords) {
        this(translation, 1, keywords);
    }

    static {
        List<String> allKeywords = new ArrayList<>();

        for (EConditionPart eConditionPart : ALL) {
            allKeywords.addAll(eConditionPart.keywords);
        }

        ALL_KEYWORDS = Collections.unmodifiableList(allKeywords);
    }
}
