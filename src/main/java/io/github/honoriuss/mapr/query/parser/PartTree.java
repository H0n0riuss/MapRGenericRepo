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

    public PartTree(String source, Class<?> clazz) {
        Assert.notNull(source, "Source must not be null");
        Matcher matcher = PREFIX_TEMPLATE.matcher(source);
        if (!matcher.find()) {
            this.subject = new Subject(Optional.empty());
        } else {
            this.subject = new Subject(Optional.of(matcher.group(0)));
            source = matcher.group(1);
        }
        createParts(source, clazz);

    }

    private void createParts(String source, Class<?> clazz) {
        var keywords = Part.Type.ALL_KEYWORDS;
        FieldUtils.getAllFieldsList(clazz).forEach(field -> keywords.add(field.getName()));

        //var splitSource = splitByKeywords(source, String.valueOf(keywords));
        //for (var split : splitSource) {
            //var part = new Part(split);
            //this.parts.add(part);
        //}
    }

    /*public static List<String> splitByKeywords(String source, String... keywords) {
        var resultList = new ArrayList<String>();
        var regex = String.join("|", keywords);
        var splitSource = source.split(regex);
        var part = "";
        for (var split : splitSource) {
            if (Part.isValidType(split)) {
                part = part.concat(split);
            }
            part = part.concat(split);
            resultList.add(part);
        }

        return resultList;
    }*/

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
