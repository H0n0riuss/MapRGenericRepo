package io.github.honoriuss.mapr.query.models;

import io.github.honoriuss.mapr.utils.Assert;
import io.github.honoriuss.mapr.utils.StringUtils;

import java.util.*;
import java.util.regex.Pattern;

import static io.github.honoriuss.mapr.query.parser.Part.Type.fromProperty;

/**
 * @author H0n0riuss
 */
public class QueryType {
    private final Pattern BY = Pattern.compile("By");
    private final List<String> queryTypeStringList;
    private final Class<?> clazz;
    private final List<Object> eQueryTypeList;

    public QueryType(String source, Class<?> clazz) {
        Assert.notNull(source, "Source cant be null");
        this.clazz = clazz;

        if (!hasQueryType(source)) {
            this.queryTypeStringList = null;
            this.eQueryTypeList = null;
            return;
        }

        var methodName = extractMethodNameAfterBy(source);
        queryTypeStringList = extractQueryTypes(methodName);
        eQueryTypeList = createEQueryList();
    }

    public QueryType(String source) {
        this(source, null);
    }

    public Optional<List<String>> getQueryTypeStringList() {
        if (queryTypeStringList == null) {
            return Optional.empty();
        }
        return Optional.of(queryTypeStringList);
    }

    public Optional<List<Object>> getEQueryTypeList() {
        if (eQueryTypeList == null) {
            return Optional.empty();
        }
        return Optional.of(eQueryTypeList);
    }

    private List<Object> createEQueryList() {
        var size = this.queryTypeStringList.size();
        var result = new ArrayList<>(size);
        var keywords = new ArrayList<>(EQueryType.ALL_KEYWORDS);
        var pattern = Pattern.compile(String.join("|", keywords));

        for (int i = 0; i < size; ++i) {
            var queryType = this.queryTypeStringList.get(i);

            if (pattern.matcher(queryType).find()) {
                var eQueryType = fromProperty(queryType);
                result.add(eQueryType);
                for (int j = 1; j <= eQueryType.getNumberOfArguments(); ++j) { //TODO validation
                    result.add(this.queryTypeStringList.get(i - j));
                }
            }
        }
        return result;
    }

    private String extractMethodNameAfterBy(String source) {
        return source.split("(By|OrderBy)")[1];
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
        LIKE("like", "Like", "IsLike"),
        LIMIT("limit", "Limit", "IsLimit"),
        BETWEEN("between", 2, "Between", "IsBetween"),
        SIMPLE_PROPERTY("is", "Is", "Equals");
        private static final List<EQueryType> ALL = Arrays.asList(LIKE, LIMIT, BETWEEN, SIMPLE_PROPERTY);
        public static final Collection<String> ALL_KEYWORDS;
        private final List<String> keywords;
        private final int numberOfArguments;
        private final String translation;

        EQueryType(String translation, String... keywords) {
            this(translation, 1, keywords);
        }

        EQueryType(String translation, int numberOfArguments, String... keywords) {
            this.translation = translation;
            this.numberOfArguments = numberOfArguments;
            this.keywords = Arrays.asList(keywords);
        }

        public int getNumberOfArguments() {
            return this.numberOfArguments;
        }

        public String getTranslation() {
            return this.translation;
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
