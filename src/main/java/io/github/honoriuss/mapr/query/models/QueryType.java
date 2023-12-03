package io.github.honoriuss.mapr.query.models;

import io.github.honoriuss.mapr.utils.Assert;
import io.github.honoriuss.mapr.utils.StringUtils;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author H0n0riuss
 */
public class QueryType {
    private final Pattern BY = Pattern.compile("By");
    private final List<String> eQueryTypeList;
    private final Class<?> clazz;

    public QueryType(String source, Class<?> clazz) {
        Assert.notNull(source, "Source cant be null");
        this.clazz = clazz;

        if (!hasQueryType(source)) {
            eQueryTypeList = null;
            return;
        }

        var methodName = extractMethodNameAfterBy(source);
        eQueryTypeList = extractQueryTypes(methodName);
    }

    public QueryType(String source) {
        this(source, null);
    }

    public Optional<List<String>> getEQueryTypeList() {
        if (eQueryTypeList == null) {
            return Optional.empty();
        }
        return Optional.of(eQueryTypeList);
    }

    private String extractMethodNameAfterBy(String source) {
        return source.split("By", 2)[1];
    }

    private List<String> extractQueryTypes(String source) {
        var resultList = new ArrayList<String>();
        source = source.split("\\(")[0];
        Assert.hasText(source, "there should be a QueryType");
        var keywords = new ArrayList<>(EQueryType.ALL_KEYWORDS);

        addOptionalClassAttributes(keywords);

        do {
            var hasKeyword = false;
            for (var keyword : keywords) {
                if (!source.startsWith(keyword)) {
                    continue;
                }
                source = source.substring(keyword.length());
                resultList.add(keyword);
                hasKeyword = true;
                break;
            }
            if (!hasKeyword) {
                throw new IllegalArgumentException("No keyword or class attribute in source");
            }
        } while (!source.isEmpty());

        return resultList;
    }

    private void addOptionalClassAttributes(Collection<String> collection) {
        if (this.getClazz().isEmpty()) {
            return;
        }
        var attributes = StringUtils.getAttributesFromClass(this.clazz);
        for (var attribute : attributes) {
            var modifiedAttribute = Character.toUpperCase(attribute.charAt(0)) + attribute.substring(1);
            collection.add(modifiedAttribute);
        }
    }

    private boolean hasQueryType(String source) {
        return BY.matcher(source).find();
    }

    private Optional<Class<?>> getClazz() {
        return this.clazz == null ? Optional.empty() : Optional.of(this.clazz);
    }

    public enum EQueryType {
        LIKE("Like", "IsLike"),
        LIMIT("Limit", "IsLimit"),
        BETWEEN("Between", "IsBetween"),
        ORDER_BY("Order", "OrderBy"),
        SIMPLE_PROPERTY("Is", "Equals");
        private static final List<EQueryType> ALL = Arrays.asList(LIKE, LIMIT, BETWEEN, ORDER_BY, SIMPLE_PROPERTY);
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
