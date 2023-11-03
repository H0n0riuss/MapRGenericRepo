package io.github.honoriuss.mapr.query.parser;

import io.github.honoriuss.mapr.utils.Assert;

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
    private final Subject subject;
    private final List<Part> parts;

    public PartTree(String source) {
        Assert.notNull(source, "Source must not be null");
        Matcher matcher = PREFIX_TEMPLATE.matcher(source);
        if (!matcher.find()) {
            this.subject = new Subject(Optional.empty());
        } else {
            this.subject = new Subject(Optional.of(matcher.group(0)));
        }
        parts = new ArrayList<>();
    }


    private static class Subject {
        //private final Pattern LIMITED_QUERY_TEMPLATE = Pattern.compile("^(find|read|get|query|search|stream)(Distinct)?(First|Top)(\\d*)?(\\p{Lu}.*?)??By");

        public Subject(Optional<String> subject) {

        }

        private boolean matches(Optional<String> subject, Pattern pattern) {
            return (Boolean)subject.map((it) -> {
                return pattern.matcher(it).find();
            }).orElse(false);
        }
    }
}
