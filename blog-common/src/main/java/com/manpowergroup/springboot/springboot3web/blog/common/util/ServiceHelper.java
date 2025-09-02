package com.manpowergroup.springboot.springboot3web.blog.common.util;

public final class ServiceHelper {

    private ServiceHelper() {}

    /**
     * 保证数值为正数，否则返回默认值
     */
    public static long safePositive(Long v, long defaultValue) {
        return (v == null || v < 0) ? defaultValue : v;
    }

    /**
     * 页码安全兜底（最小为1）
     */
    public static long safePageNum(Long v) {
        return (v == null || v < 1) ? 1L : v;
    }

    /**
     * 页大小安全兜底（最小1，最大100，默认10）
     */
    public static long safePageSize(Long v) {
        long size = (v == null || v < 1) ? 10L : v;
        long max = 100L;
        return Math.min(size, max);
    }
}
