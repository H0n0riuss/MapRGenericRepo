package io.github.honoriuss.mapr.query.parser;

import io.github.honoriuss.mapr.utils.Assert;
import oadd.org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author H0n0riuss
 */
public class PartTree {
    private static final Pattern PREFIX_TEMPLATE = Pattern.compile("^(save|find|read|get|query|search|stream|count|exists|delete|remove)((\\p{Lu}.*?))??By");

    private static final String SPLITTER = "(?=[A-Z])";
    private final Subject subject;
    private final List<Part> parts = new ArrayList<>();
    private final Class<?> clazz;

    public PartTree(String source, Class<?> clazz) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(clazz, "Class cant be null"); //TODO may it can be null?
        this.clazz = clazz;
        Matcher matcher = PREFIX_TEMPLATE.matcher(source);
        if (!matcher.find()) {
            this.subject = new Subject(Optional.empty());
        } else {
            this.subject = new Subject(Optional.of(matcher.group()));
            source = source.substring(matcher.group().length());
        }

        var StringParts = extractStringParts(source);
        createParts(StringParts);
    }

    private MethodParts parseMethodName(String methodName) {
        MethodParts methodParts = new MethodParts();

        Matcher matcher = PREFIX_TEMPLATE.matcher(methodName);

        if (matcher.find()) {
            methodParts.prefix = matcher.group();
            methodName = methodName.substring(matcher.end());
        }

        // Sucht nach den Attributen im Methodennamen
        Pattern attributePattern = Pattern.compile("([A-Z][a-z]*)");
        matcher = attributePattern.matcher(methodName);

        while (matcher.find()) {
            methodParts.attributes.add(matcher.group());
        }

        return methodParts;
    }

    public class MethodParts {
        String prefix = "";
        List<String> attributes = new ArrayList<>();
    }

    private void createParts(List<String> stringParts) {
        var keywords = Part.Type.ALL_KEYWORDS;
        for (int i = 0; i < stringParts.size(); ++i) {
            var StringPart = stringParts.get(i);
            Part part;
            if (keywords.contains(StringPart)) {
                part = new Part(StringPart);
                for (int j = 1; j < part.getType().getNumberOfArguments(); ++j) {
                    part.addProperty(stringParts.get(i + j));
                    ++i;
                }
                parts.add(part);
            }
        }
    }

    private List<String> extractStringParts(String source) {
        var extractedKeywords = new ArrayList<String>();
        var keywords = new ArrayList<>(Part.Type.ALL_KEYWORDS);
        FieldUtils.getAllFieldsList(clazz).forEach(field -> keywords.add(field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1)));

        while (source.length() > 0) {
            boolean keywordFound = false;
            for (String keyword : keywords) {
                if (source.startsWith(keyword)) {
                    source = source.substring(keyword.length());
                    keywordFound = true;
                    extractedKeywords.add(keyword);
                    break;
                }
            }

            if (!keywordFound) {
                throw new IllegalArgumentException("No keyword or attribute matches the method name.");
            }
        }
        return extractedKeywords;
    }

    private static class Subject {
        private static final String DISTINCT = "Distinct";
        private static final Pattern COUNT_BY_TEMPLATE = Pattern.compile("^count(\\p{Lu}.*?)??By");
        private static final Pattern EXISTS_BY_TEMPLATE = Pattern.compile("^(exists)(\\p{Lu}.*?)??By");
        private static final Pattern DELETE_BY_TEMPLATE = Pattern.compile("^(delete|remove)(\\p{Lu}.*?)??By");
        private static final String LIMITING_QUERY_PATTERN = "(First|Top)(\\d*)?";
        private static final Pattern LIMITED_QUERY_TEMPLATE = Pattern.compile("^(find|read|get|query|search|stream)(Distinct)?(First|Top)(\\d*)?(\\p{Lu}.*?)??By");
        private final boolean distinct;
        private final boolean count;
        private final boolean exists;
        private final boolean delete;
        private final Optional<Integer> maxResults;

        public Subject(Optional<String> subject) {
            this.distinct = (Boolean)subject.map((it) -> {
                return it.contains("Distinct");
            }).orElse(false);
            this.count = this.matches(subject, COUNT_BY_TEMPLATE);
            this.exists = this.matches(subject, EXISTS_BY_TEMPLATE);
            this.delete = this.matches(subject, DELETE_BY_TEMPLATE);
            this.maxResults = this.returnMaxResultsIfFirstKSubjectOrNull(subject);
        }

        private Optional<Integer> returnMaxResultsIfFirstKSubjectOrNull(Optional<String> subject) {
            return subject.map((it) -> {
                Matcher grp = LIMITED_QUERY_TEMPLATE.matcher(it);
                return !grp.find() ? null : StringUtils.hasText(grp.group(4)) ? Integer.valueOf(grp.group(4)) : 1;
            });
        }

        public boolean isDelete() {
            return this.delete;
        }

        public boolean isCountProjection() {
            return this.count;
        }

        public boolean isExistsProjection() {
            return this.exists;
        }

        public boolean isDistinct() {
            return this.distinct;
        }

        public Optional<Integer> getMaxResults() {
            return this.maxResults;
        }

        private boolean matches(Optional<String> subject, Pattern pattern) {
            return (Boolean)subject.map((it) -> {
                return pattern.matcher(it).find();
            }).orElse(false);
        }
    }
}
