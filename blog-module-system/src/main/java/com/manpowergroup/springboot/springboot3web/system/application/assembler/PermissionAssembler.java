package com.manpowergroup.springboot.springboot3web.system.application.assembler;

import com.manpowergroup.springboot.springboot3web.blog.common.util.StringUtils;
import com.manpowergroup.springboot.springboot3web.system.application.dto.permission.PermissionSaveOrUpdateRequest;
import com.manpowergroup.springboot.springboot3web.system.domain.model.permission.Permission;


/**
 * Permission のリクエストDTO ⇔ Entity 変換ユーティリティ
 * 目的：
 * - Service から normalize / default 値埋め を分離して読みやすくする
 * - DTO は「入力 + 検証」に専念し、変換はここで一元化する
 */
public final class PermissionAssembler {

    private PermissionAssembler() {
    }

    /**
     * 新規作成用：Request -> Entity
     */
    public static Permission toNewEntity(PermissionSaveOrUpdateRequest req) {
        return Permission.builder()
                .parentId(req.parentId())
                .name(normalizeName(req.name()))
                .code(normalizeCode(req.code()))
                .type(req.type())
                .path(normalizePath(req.path()))
                .method(req.method())
                .sort(defaultSort(req.sort()))
                .status(req.status())
                .build();
    }

    /**
     * 更新用：Request -> 既存Entityへ反映
     */
    public static void applyToExisting(PermissionSaveOrUpdateRequest req, Permission existing) {
        existing.setParentId(req.parentId());
        existing.setName(normalizeName(req.name()));
        existing.setCode(normalizeCode(req.code()));
        existing.setType(req.type());
        existing.setPath(normalizePath(req.path()));
        existing.setMethod(req.method());
        existing.setSort(defaultSort(req.sort()));
        existing.setStatus(req.status());
    }

    /**
     * 権限名：トリム・全角半角など normalize
     */
    private static String normalizeName(String name) {
        return StringUtils.normalize(name);
    }

    /**
     * 権限制御コード：基本は normalize のみ
     * - role.code は大文字統一でも良いが、permission.code は「user:add」など小文字文化が多いので、
     * まずは大小変換せずに統一（必要ならここで方針を変える）
     */
    private static String normalizeCode(String code) {
        // 必要なら：.toLowerCase(Locale.ROOT) にする
        return StringUtils.normalize(code);
    }

    /**
     * path：空文字は null 扱いにして保存（DB をきれいにする）
     */
    private static String normalizePath(String path) {
        final var p = StringUtils.normalize(path);
        return (p == null || p.isBlank()) ? null : p;
    }

    /**
     * sort：null は 0 扱い（要件に合わせて変更可）
     */
    private static Integer defaultSort(Integer sort) {
        return sort == null ? 0 : sort;
    }
}