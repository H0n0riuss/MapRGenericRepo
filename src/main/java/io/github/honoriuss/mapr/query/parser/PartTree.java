package io.github.honoriuss.mapr.query.parser;

import io.github.honoriuss.mapr.utils.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    }

    private static String[] split(String text, String keyword) {
        Pattern pattern = Pattern.compile(String.format("(%s)(?=(\\p{Lu}|\\P{InBASIC_LATIN}))", keyword));
        return pattern.split(text);
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

    private static class Predicate {
        private static final Pattern ALL_IGNORE_CASE = Pattern.compile("AllIgnor(ing|e)Case");
        private static final String ORDER_BY = "OrderBy";
        private final List<OrPart> nodes;
        //private final OrderBySource orderBySource;
        private boolean alwaysIgnoreCase;

        public Predicate(String predicate, Class<?> domainClass) {
            String[] parts = PartTree.split(this.detectAndSetAllIgnoreCase(predicate), "OrderBy");
            if (parts.length > 2) {
                throw new IllegalArgumentException("OrderBy must not be used more than once in a method name");
            } else {
                this.nodes = (List) Arrays.stream(PartTree.split(parts[0], "Or")).filter(StringUtils::hasText).map((part) -> {
                    return new OrPart(part, domainClass, this.alwaysIgnoreCase);
                }).collect(Collectors.toList());
          //      this.orderBySource = parts.length == 2 ? new OrderBySource(parts[1], Optional.of(domainClass)) : OrderBySource.EMPTY;
            }
        }

        private String detectAndSetAllIgnoreCase(String predicate) {
            Matcher matcher = ALL_IGNORE_CASE.matcher(predicate);
            if (matcher.find()) {
                this.alwaysIgnoreCase = true;
                predicate = predicate.substring(0, matcher.start()) + predicate.substring(matcher.end(), predicate.length());
            }

            return predicate;
        }

      /*  public OrderBySource getOrderBySource() {
            return this.orderBySource;
        }*/

        public Iterator<OrPart> iterator() {
            return this.nodes.iterator();
        }
    }

    public static class OrPart {
        private final List<Part> children;

        OrPart(String source, Class<?> domainClass, boolean alwaysIgnoreCase) {
            String[] split = PartTree.split(source, "And");
            this.children = (List)Arrays.stream(split).filter(StringUtils::hasText).map((part) -> {
                return new Part(part/*, domainClass, alwaysIgnoreCase*/);
            }).collect(Collectors.toList());
        }

        public Iterator<Part> iterator() {
            return this.children.iterator();
        }

        public String toString() {
            return StringUtils.collectionToDelimitedString(this.children, " and ");
        }
    }
}
