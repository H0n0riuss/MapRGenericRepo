package io.github.honoriuss.mapr.query.parser;

import org.springframework.util.Assert;

import java.beans.Introspector;
import java.util.*;

/**
 * @author H0n0riuss
 */
@Deprecated
public class Part {
    private final Type type;
    private final List<String> properties;

    public Part(String source) {
        Assert.hasText(source, "Part source must not be null or empty");

        this.type = Part.Type.fromProperty(source); //TODO no need if PartTrees decides
        properties = new ArrayList<>();
    }

    public Type getType() {
        return this.type;
    }

    public List<String> getProperties() {
        return this.properties;
    }

    public static boolean isValidType(String input) {
        try {
            Type.valueOf(input);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public void addProperty(String newProperty) {
        properties.add(newProperty);
    }

    public enum Type {
        BETWEEN(2, "IsBetween", "Between"),
        IS_NOT_NULL(0, "IsNotNull", "NotNull"),
        IS_NULL(0, "IsNull", "Null"),
        LESS_THAN("IsLessThan", "LessThan"),
        LESS_THAN_EQUAL("IsLessThanEqual", "LessThanEqual"),
        GREATER_THAN("IsGreaterThan", "GreaterThan"),
        GREATER_THAN_EQUAL("IsGreaterThanEqual", "GreaterThanEqual"),
        BEFORE("IsBefore", "Before"),
        AFTER("IsAfter", "After"),
        NOT_LIKE("IsNotLike", "NotLike"),
        LIKE("IsLike", "Like"),
        STARTING_WITH("IsStartingWith", "StartingWith", "StartsWith"),
        ENDING_WITH("IsEndingWith", "EndingWith", "EndsWith"),
        IS_NOT_EMPTY(0, "IsNotEmpty", "NotEmpty"),
        IS_EMPTY(0, "IsEmpty", "Empty"),
        NOT_CONTAINING("IsNotContaining", "NotContaining", "NotContains"),
        CONTAINING("IsContaining", "Containing", "Contains"),
        NOT_IN("IsNotIn", "NotIn"),
        IN("IsIn", "In"),
        NEAR("IsNear", "Near"),
        WITHIN("IsWithin", "Within"),
        REGEX("MatchesRegex", "Matches", "Regex"),
        EXISTS(0, "Exists"),
        TRUE(0, "IsTrue", "True"),
        FALSE(0, "IsFalse", "False"),
        NEGATING_SIMPLE_PROPERTY("IsNot", "Not"),
        SIMPLE_PROPERTY("Is", "Equals");

        private static final List<Type> ALL = Arrays.asList(IS_NOT_NULL, IS_NULL, BETWEEN, LESS_THAN, LESS_THAN_EQUAL, GREATER_THAN, GREATER_THAN_EQUAL, BEFORE, AFTER, NOT_LIKE, LIKE, STARTING_WITH, ENDING_WITH, IS_NOT_EMPTY, IS_EMPTY, NOT_CONTAINING, CONTAINING, NOT_IN, IN, NEAR, WITHIN, REGEX, EXISTS, TRUE, FALSE, NEGATING_SIMPLE_PROPERTY, SIMPLE_PROPERTY);
        public static final Collection<String> ALL_KEYWORDS;
        private final List<String> keywords;
        private final int numberOfArguments;

        Type(int numberOfArguments, String... keywords) {
            this.numberOfArguments = numberOfArguments;
            this.keywords = Arrays.asList(keywords);
        }

        Type(String... keywords) {
            this(1, keywords);
        }

        public static Type fromProperty(String rawProperty) {
            Iterator<Type> iterator = ALL.iterator();

            Type type;
            do {
                if (!iterator.hasNext()) {
                    return SIMPLE_PROPERTY;
                }

                type = iterator.next();
            } while (!type.supports(rawProperty));

            return type;
        }

        public Collection<String> getKeywords() {
            return Collections.unmodifiableList(this.keywords);
        }

        private boolean supports(String property) {
            Iterator<String> iterator = this.keywords.iterator();

            String keyword;
            do {
                if (!iterator.hasNext()) {
                    return false;
                }

                keyword = iterator.next();
            } while (!property.endsWith(keyword));

            return true;
        }

        public int getNumberOfArguments() {
            return this.numberOfArguments;
        }

        public String extractProperty(String part) {
            String candidate = Introspector.decapitalize(part);
            Iterator<String> iterator = this.keywords.iterator();

            String keyword;
            do {
                if (!iterator.hasNext()) {
                    return candidate;
                }

                keyword = iterator.next();
            } while (!candidate.endsWith(keyword));

            return candidate.substring(0, candidate.length() - keyword.length());
        }

        public String toString() {
            return String.format("%s (%s): %s", this.name(), this.getNumberOfArguments(), this.getKeywords());
        }

        static {
            List<String> allKeywords = new ArrayList<>();

            for (Type type : ALL) {
                allKeywords.addAll(type.keywords);
            }

            ALL_KEYWORDS = Collections.unmodifiableList(allKeywords);
        }
    }
}
