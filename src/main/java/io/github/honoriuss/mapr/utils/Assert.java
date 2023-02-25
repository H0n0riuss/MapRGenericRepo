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
}
