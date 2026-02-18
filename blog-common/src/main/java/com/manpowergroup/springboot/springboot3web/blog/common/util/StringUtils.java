package com.manpowergroup.springboot.springboot3web.blog.common.util;

public final class StringUtils {

    private StringUtils() {}

    /**
     * null → ""
     */
    public static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    /**
     * "" or blank → null
     */
    public static String emptyToNull(String value) {
        return hasText(value) ? value.trim() : null;
    }

    /**
     * 判断是否有文本（非 null 且非空白）
     */
    public static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    /**
     * 去除前后空格（null 安全）
     */
    public static String trim(String value) {
        return value == null ? null : value.trim();
    }

    /**
     * null/blank → null
     */
    public static String normalize(String value) {
        return hasText(value) ? value.trim() : null;
    }
}
