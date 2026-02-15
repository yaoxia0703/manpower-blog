package com.manpowergroup.springboot.springboot3web.blog.common.util;

import java.util.Objects;

public final class StringUtils {

    private StringUtils() {
    }

    /**
     * null → ""
     */
    public static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    /**
     * "" → null
     */
    public static String emptyToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }

    /**
     * 判断是否有文本
     */
    public static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
