package io.github.honoriuss.mapr.query.models;

import io.github.honoriuss.mapr.utils.Assert;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author H0n0riuss
 */
public class QueryType {
    private final Pattern BY = Pattern.compile("By");
    private final List<EQueryType> eQueryTypeList;

    public QueryType(String source) {
        Assert.notNull(source, "Source cant be null");
        if (!hasQueryType(source)) {
            eQueryTypeList = null;
            return;
        }

        var methodName = extractMethodNameAfterBy(source);
        eQueryTypeList = extractQueryTypes(methodName);
    }

    public Optional<List<EQueryType>> getEQueryTypeList() {
        if (eQueryTypeList == null) {
            return Optional.empty();
        }
        return Optional.of(eQueryTypeList);
    }

    private String extractMethodNameAfterBy(String source) {
        return source.split("By")[1];
    }

    private List<EQueryType> extractQueryTypes(String source) {
        var resultList = new ArrayList<EQueryType>();
        source = source.split("\\(")[0];

        do {
            //TODO durch enum iterieren, wenn keyword gefunden, dann Liste hinzufügen mit String davor... und splitten

        } while (source.length() != 0);

        return resultList;
    }

    private boolean hasQueryType(String source) {
        return BY.matcher(source).find();
    }

    public enum EQueryType {
        LIKE("Like", "IsLike"),
        LIMIT("Limit", "IsLimit"),
        BETWEEN("Between", "IsBetween"),
        SIMPLE_PROPERTY("Is", "Equals");
        private static final List<EQueryType> ALL = Arrays.asList(LIKE, LIMIT, BETWEEN, SIMPLE_PROPERTY);
        public static final Collection<String> ALL_KEYWORDS;
        private final List<String> keywords;
        private final int numberOfArguments;

        EQueryType(String... keywords) {
            this(1, keywords);
        }

        EQueryType(int numberOfArguments, String... keywords) {
            this.numberOfArguments = numberOfArguments;
            this.keywords = Arrays.asList(keywords);
        }

        public int getNumberOfArguments() {
            return this.numberOfArguments;
        }

        public EQueryType fromProperty(String rawProperty) {
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
}
