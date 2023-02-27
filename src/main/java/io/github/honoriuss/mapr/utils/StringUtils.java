package io.github.honoriuss.mapr.utils;

import javax.annotation.Nullable;

/**
 * @author H0n0riuss
 */
public abstract class StringUtils {
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
        } else {
            int extIndex = path.lastIndexOf(46);
            if (extIndex == -1) {
                return null;
            } else {
                int folderIndex = path.lastIndexOf(47);
                return folderIndex > extIndex ? null : path.substring(extIndex + 1);
            }
        }
    }
    @Nullable
    public static boolean hasText(String str){
        return str != null && !str.equals("");
    }
}
