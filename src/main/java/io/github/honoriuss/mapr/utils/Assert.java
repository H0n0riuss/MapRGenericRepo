package io.github.honoriuss.mapr.utils;

import javax.annotation.Nullable;

/**
 * @author H0n0riuss
 */
public abstract class Assert {
    public static void notNull(@Nullable Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void hasText(@Nullable String text, String message) {
        if (!StringUtils.hasText(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void containsString(String text, String shouldContain, String message) {
        notNull(text, "text cant be null");
        if (!text.contains(shouldContain)) {
            throw new IllegalArgumentException(message);
        }
    }
}
