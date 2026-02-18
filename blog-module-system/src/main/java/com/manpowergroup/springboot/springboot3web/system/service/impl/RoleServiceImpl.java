package com.manpowergroup.springboot.springboot3web.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.JoinPageResult;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.blog.common.util.StringUtils;
import com.manpowergroup.springboot.springboot3web.system.dto.role.RoleQueryRequest;
import com.manpowergroup.springboot.springboot3web.system.dto.role.RoleSaveOrUpdateRequest;
import com.manpowergroup.springboot.springboot3web.system.entity.Role;
import com.manpowergroup.springboot.springboot3web.system.mapper.RoleMapper;
import com.manpowergroup.springboot.springboot3web.system.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.PageRequest;
import com.manpowergroup.springboot.springboot3web.blog.common.util.PageUtil;


import java.util.Locale;

@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final PageUtil pageUtil;

    public RoleServiceImpl(PageUtil pageUtil) {
        this.pageUtil = pageUtil;
    }

    @Override
    public JoinPageResult<Role> pageRoles(
            PageRequest pageRequest,
            RoleQueryRequest query
    ) {

        final Page<Role> page =
                pageUtil.toPage(pageRequest == null ? new com.manpowergroup.springboot.springboot3web.blog.common.dto.PageRequest() : pageRequest);

        final var qw = new LambdaQueryWrapper<Role>()
                .orderByAsc(Role::getSort)
                .orderByDesc(Role::getId);

        if (query != null) {
            final var keyword = StringUtils.normalize(query.keyword());
            qw.and(keyword != null, w ->
                            w.like(Role::getCode, keyword)
                                    .or()
                                    .like(Role::getName, keyword)
                    )
                    .eq(query.status() != null, Role::getStatus, query.status());
        }

        final var result = baseMapper.selectPage(page, qw);

        return JoinPageResult.of(
                result.getRecords(),
                result.getTotal(),
                result.getCurrent(),
                result.getSize()
        );
    }


    @Override
    public Role getRoleById(Long id) {
        if (id == null) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "ロールIDが指定されていません。");
        }

        final var role = baseMapper.selectById(id);
        if (role == null) {
            throw BizException.withDetail(ErrorCode.NOT_FOUND, "ロールが存在しません。id=" + id);
        }
        return role;
    }

    @Override
    @Transactional
    public Long createRole(RoleSaveOrUpdateRequest request) {
        validateRequest(request);

        final var code = StringUtils.normalize(request.code()).toUpperCase(Locale.ROOT);
        ensureCodeUnique(code, null);

        final var name = StringUtils.normalize(request.name());
        final var status = normalizeStatus(request.status());
        final var sort = request.sort() == null ? 0 : request.sort();

        final var role = Role.builder()
                .code(code)
                .name(name)
                .sort(sort)
                .status(status)
                .build();

        baseMapper.insert(role);

        // ★ 必須ログ：登録（監査・トラブルシュート用）
        log.info("ロールを登録しました。id={}, code={}, name={}, status={}, sort={}",
                role.getId(), code, name, status, sort);

        return role.getId();
    }

    @Override
    @Transactional
    public void updateRole(Long id, RoleSaveOrUpdateRequest request) {

        if (id == null) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "ロールIDが指定されていません。");
        }
        validateRequest(request);

        final var existing = getRoleById(id);

        final var code = StringUtils.normalize(request.code()).toUpperCase(Locale.ROOT);
        if (!code.equals(existing.getCode())) {
            ensureCodeUnique(code, id);
            existing.setCode(code);
        }

        final var name = StringUtils.normalize(request.name());
        final var status = normalizeStatus(request.status());
        final var sort = request.sort() == null ? 0 : request.sort();

        existing.setName(name)
                .setStatus(status)
                .setSort(sort);

        baseMapper.updateById(existing);

        // ★ 必須ログ：更新
        log.info("ロールを更新しました。id={}, code={}, name={}, status={}, sort={}",
                existing.getId(), existing.getCode(), existing.getName(), existing.getStatus(), existing.getSort());
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        if (id == null) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "ロールIDが指定されていません。");
        }

        // 削除前に存在確認（ログに code/name を残せる＆NOT_FOUNDを明確化）
        final var existing = getRoleById(id);

        final var affected = baseMapper.deleteById(id);
        if (affected == 0) {
            throw BizException.withDetail(ErrorCode.NOT_FOUND, "ロールが存在しません。id=" + id);
        }

        // ★ 必須ログ：削除（論理削除でも記録）
        log.info("ロールを削除しました。id={}, code={}, name={}",
                existing.getId(), existing.getCode(), existing.getName());
    }

    @Override
    @Transactional
    public void changeStatus(Long id, Byte status) {
        if (id == null) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "ロールIDが指定されていません。");
        }

        final var role = getRoleById(id);

        final var newStatus = normalizeStatus(status);
        final var oldStatus = role.getStatus();

        role.setStatus(newStatus);
        baseMapper.updateById(role);

        // ★ 必須ログ：状態変更（運用でよく追う）
        log.info("ロールの状態を変更しました。id={}, code={}, name={}, status:{} -> {}",
                role.getId(), role.getCode(), role.getName(), oldStatus, newStatus);
    }

    private void validateRequest(RoleSaveOrUpdateRequest request) {
        if (request == null) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "リクエストが不正です。");
        }
        if (!StringUtils.hasText(request.code())) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "ロールコードは必須です。");
        }
        if (!StringUtils.hasText(request.name())) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "ロール名は必須です。");
        }

        final var code = StringUtils.normalize(request.code());
        if (code == null || !code.matches("^[A-Za-z0-9_]+$")) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "ロールコード形式が不正です。（英数字と_のみ）");
        }
    }

    private void ensureCodeUnique(String code, Long excludeId) {

        final var qw = new LambdaQueryWrapper<Role>()
                .eq(Role::getCode, code);

        if (excludeId != null) {
            qw.ne(Role::getId, excludeId);
        }

        if (baseMapper.selectCount(qw) > 0) {
            throw BizException.withDetail(ErrorCode.CONFLICT, "ロールコードは既に存在します。code=" + code);
        }
    }

    private Byte normalizeStatus(Byte status) {
        if (status == null) return (byte) 1;
        if (status != 0 && status != 1) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "status は 0/1 のみ指定可能です。");
        }
        return status;
    }
}
