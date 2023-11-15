package io.github.honoriuss.mapr.query.parser;

import io.github.honoriuss.mapr.utils.Assert;
import oadd.org.apache.commons.lang3.reflect.FieldUtils;

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
            this.subject = new Subject(Optional.of(matcher.group(0)));
            source = source.substring(matcher.group(0).length());
        }

        var StringParts = extractStringParts(source);
        createParts(StringParts);
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
        FieldUtils.getAllFieldsList(clazz).forEach(field -> keywords.add(field.getName()));

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
        //private final Pattern LIMITED_QUERY_TEMPLATE = Pattern.compile("^(find|read|get|query|search|stream)(Distinct)?(First|Top)(\\d*)?(\\p{Lu}.*?)??By");

        public Subject(Optional<String> subject) {

        }

        private boolean matches(Optional<String> subject, Pattern pattern) {
            return (Boolean) subject.map((it) -> {
                return pattern.matcher(it).find();
            }).orElse(false);
        }
    }
}
