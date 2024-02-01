package io.github.honoriuss.mapr.generator;

import java.util.*;

/**
 * @author H0n0riuss
 */
enum ECrudType {
    CREATE("insert", "create", "insert", "save"),
    READ("find", "read", "find", "get"),
    UPDATE("replace", "update", "replace"),
    DELETE("delete", "delete", "remove");
    private static final List<ECrudType> ALL = Arrays.asList(CREATE, READ, UPDATE, DELETE);
    public static final Collection<String> ALL_KEYWORDS;
    private final String translation;
    private final List<String> keywords;

    ECrudType(String translation, String... keywords) {
        this.translation = translation;
        this.keywords = Arrays.asList(keywords);
    }

    public static ECrudType getCrudTypeFromProperty(String methodName) {
        Iterator<ECrudType> iterator = ALL.iterator();

        ECrudType crudType;
        do {
            crudType = iterator.next();
        } while (!crudType.supports(methodName));

        return crudType;
    }

    public static String fromProperty(String methodName) {
        Iterator<ECrudType> iterator = ALL.iterator();

        ECrudType crudType;
        do {
            crudType = iterator.next();
        } while (!crudType.supports(methodName));

        return crudType.translation;
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

        for (var crudType : ALL) {
            allKeywords.addAll(crudType.keywords);
        }

        ALL_KEYWORDS = Collections.unmodifiableList(allKeywords);
    }
}
