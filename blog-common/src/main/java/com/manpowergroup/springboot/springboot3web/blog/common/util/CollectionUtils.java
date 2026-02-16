package com.manpowergroup.springboot.springboot3web.blog.common.util;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Collection 共通ユーティリティ
 * - null-safe
 * - 빈判定
 */
public final class CollectionUtils {

    private CollectionUtils() {
        // utility class
    }

    /**
     * null の List を空 List に変換する（null-safe）
     */
    public static <T> List<T> safeList(List<T> list) {
        return Objects.requireNonNullElse(list, List.of());
    }

    /**
     * Collection が null または空かどうか
     */
    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

    /**
     * Collection が空ではないかどうか
     */
    public static boolean isNotEmpty(Collection<?> c) {
        return !isEmpty(c);
    }
}
