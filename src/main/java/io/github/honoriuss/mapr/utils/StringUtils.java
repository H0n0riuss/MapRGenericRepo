package io.github.honoriuss.mapr.utils;

import oadd.org.apache.commons.lang3.reflect.FieldUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author H0n0riuss
 */
public abstract class StringUtils {
    private static final Pattern MODIFIER = Pattern.compile("(public|private|package)");

    public static boolean hasLength(@Nullable String str) {
        return str != null && !str.isEmpty();
    }

    @Nullable
    public static String getFilename(@Nullable String path) {
        if (path == null) {
            return null;
        } else {
            int separatorIndex = path.lastIndexOf(47);
            return separatorIndex != -1 ? path.substring(separatorIndex + 1) : path;
        }
    }

    @Nullable
    public static String getFilenameExtension(@Nullable String path) {
        if (path == null) {
            return null;
        }
        int extIndex = path.lastIndexOf(46);
        if (extIndex == -1) {
            return null;
        } else {
            int folderIndex = path.lastIndexOf(47);
            return folderIndex > extIndex ? null : path.substring(extIndex + 1);
        }
    }

    public static boolean hasText(String str) {
        return str != null && !str.isEmpty();
    }

    public static String extractMethodName(String source) {
        Assert.notNull(source, "Source cant be null");
        if (MODIFIER.matcher(source).find()) {
            source = source.split(" ", 2)[1];
        }
        var split = source.split(" ", 2);
        if (split.length > 1) {
            source = split[1];
        }
        return source;
    }

    public static List<String> getAttributesFromClass(Class<?> clazz) {
        Assert.notNull(clazz, "Class cant be null");

        return FieldUtils.getAllFieldsList(clazz)
                .stream()
                .map(Field::getName)
                .collect(Collectors.toList());
    }
}
