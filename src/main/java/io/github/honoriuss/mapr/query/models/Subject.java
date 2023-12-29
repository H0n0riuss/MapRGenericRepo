package io.github.honoriuss.mapr.query.models;

import io.github.honoriuss.mapr.utils.Assert;
import io.github.honoriuss.mapr.utils.StringUtils;

import javax.lang.model.element.TypeElement;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author H0n0riuss
 */
public class Subject {
    private static final Pattern SAVE_PREFIX = Pattern.compile("^(save|insert|create)"); //TODO delete?
    private static final Pattern DELETE_PREFIX = Pattern.compile("^(remove|delete)");
    private static final Pattern READ_PREFIX = Pattern.compile("^(read|find|get)");
    private static final Pattern UPDATE_PREFIX = Pattern.compile("^(update|replace)");
    private static final Pattern ALL_PREFIX = Pattern.compile("(save|insert|create|remove|delete|read|find|get|update|replace)");

    private final ESubjectType subjectType;

    public Subject(TypeElement typeElement){
        this.subjectType = extractSubjectType(typeElement.getSimpleName().toString());
    }

    public Subject(String source) {
        Assert.notNull(source, "Source cant be null");
        var methodName = StringUtils.extractMethodName(source);
        if (!ALL_PREFIX.matcher(methodName).find()) //TODO kann eigentlich auch empty sein?
            throw new IllegalArgumentException("No Subject provided");
        this.subjectType = extractSubjectType(methodName);
    }

    public Optional<ESubjectType> getSubjectType() {
        if (this.subjectType == null)
            return Optional.empty();
        return Optional.of(this.subjectType);
    }

    private ESubjectType extractSubjectType(String methodName) {
        ESubjectType subjectType = null;
        var subject = ESubjectType.getSubjectType(methodName);
        if (subject.isPresent()) {
            subjectType = subject.get();
        }
        return subjectType;
    }

    public enum ESubjectType {
        CREATE("create", "save", "insert"),
        READ("read", "get", "find"),
        UPDATE("update", "replace"),
        DELETE("delete", "remove");
        private static final List<ESubjectType> ALL = Arrays.asList(CREATE, READ, UPDATE, DELETE);
        public static final Collection<String> ALL_KEYWORDS;
        private final List<String> keywords;

        ESubjectType(String... keywords) {
            this.keywords = Arrays.asList(keywords);
        }

        public static Optional<ESubjectType> getSubjectType(String methodName) {
            for (var keyword : ALL_KEYWORDS) {
                if (methodName.startsWith(keyword)) {
                    return Arrays.stream(ESubjectType.values())
                            .filter(k -> k.keywords.contains(keyword))
                            .findFirst();
                }
            }
            return Optional.empty();
        }

        static {
            List<String> allKeywords = new ArrayList<>();

            for (ESubjectType type : ALL) {
                allKeywords.addAll(type.keywords);
            }

            ALL_KEYWORDS = Collections.unmodifiableList(allKeywords);
        }
    }
}
