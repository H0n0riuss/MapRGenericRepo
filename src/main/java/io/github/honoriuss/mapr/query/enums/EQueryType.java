package io.github.honoriuss.mapr.query.enums;

import java.util.*;

public enum EQueryType {
    LIKE("like", "Like", "IsLike"),
    LIMIT("limit", false, "Limit", "IsLimit"),
    BETWEEN("between", 2, "Between", "IsBetween"),
    ORDER_BY("orderBy", "OrderBy", "orderBy", "orderby"),
    SIMPLE_PROPERTY("is", false, "Is", "Equals");
    private static final List<EQueryType> ALL = Arrays.asList(LIKE, LIMIT, BETWEEN, ORDER_BY, SIMPLE_PROPERTY);
    public static final Collection<String> ALL_KEYWORDS;
    private final List<String> keywords;
    private final int numberOfArguments;
    private final String translation;
    private final boolean hasColumnName;

    EQueryType(String translation, String... keywords) {
        this(translation, 1, true, keywords);
    }

    EQueryType(String translation, int numberOfArguments, String... keywords) {
        this(translation, numberOfArguments, true, keywords);
    }

    EQueryType(String translation, boolean hasColumnName, String... keywords) {
        this(translation, 1, hasColumnName, keywords);
    }

    EQueryType(String translation, int numberOfArguments, boolean hasColumnName, String... keywords) {
        this.translation = translation;
        this.numberOfArguments = numberOfArguments;
        this.keywords = Arrays.asList(keywords);
        this.hasColumnName = hasColumnName;
    }

    public int getNumberOfArguments() {
        return this.numberOfArguments;
    }

    public boolean hasColumnName() {
        return this.hasColumnName;
    }

    public String getTranslation() {
        return this.translation;
    }

    public static EQueryType fromProperty(String rawProperty) {
        Iterator<EQueryType> iterator = ALL.iterator();

        EQueryType eQueryType;
        do {
            if (!iterator.hasNext()) {
                return SIMPLE_PROPERTY;
            }

            eQueryType = iterator.next();
        } while (!eQueryType.supports(rawProperty));

        return eQueryType;
    }

    private boolean supports(String source) {
        Iterator<String> iterator = this.keywords.iterator();

        String keyword;
        do {
            if (!iterator.hasNext()) {
                return false;
            }

            keyword = iterator.next();
        } while (!source.endsWith(keyword));
        return true;
    }

    static {
        List<String> allKeywords = new ArrayList<>();

        for (EQueryType eQueryType : ALL) {
            allKeywords.addAll(eQueryType.keywords);
        }

        ALL_KEYWORDS = Collections.unmodifiableList(allKeywords);
    }
}