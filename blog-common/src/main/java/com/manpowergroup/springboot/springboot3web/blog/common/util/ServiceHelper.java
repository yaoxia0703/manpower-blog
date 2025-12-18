package com.manpowergroup.springboot.springboot3web.blog.common.util;

/**
 * サービス層で使用する数値チェック用ユーティリティ
 */
public final class ServiceHelper {

    private ServiceHelper() {
        // インスタンス生成禁止
    }

    /**
     * 正の数値であることを保証する。
     * null または 0 未満の場合はデフォルト値を返す。
     *
     * @param v            対象値
     * @param defaultValue デフォルト値
     * @return 安全な数値
     */
    public static long safePositive(Long v, long defaultValue) {
        return (v == null || v < 0) ? defaultValue : v;
    }

    /**
     * ページ番号の安全チェック。
     * 最小値は 1 とする。
     *
     * @param v ページ番号
     * @return 安全なページ番号
     */
    public static long safePageNum(Long v) {
        return (v == null || v < 1) ? 1L : v;
    }

    /**
     * ページサイズの安全チェック。
     * 最小 1、最大 100、未指定時は 10。
     *
     * @param v ページサイズ
     * @return 安全なページサイズ
     */
    public static long safePageSize(Long v) {
        long size = (v == null || v < 1) ? 10L : v;
        long max = 100L;
        return Math.min(size, max);
    }
}
