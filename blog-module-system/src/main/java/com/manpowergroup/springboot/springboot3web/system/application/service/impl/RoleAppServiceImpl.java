package com.manpowergroup.springboot.springboot3web.system.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.JoinPageResult;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.PageRequest;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.Status;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.blog.common.util.PageUtil;
import com.manpowergroup.springboot.springboot3web.blog.common.util.StringUtils;
import com.manpowergroup.springboot.springboot3web.system.application.dto.role.RoleQueryRequest;
import com.manpowergroup.springboot.springboot3web.system.application.dto.role.RoleSaveOrUpdateRequest;
import com.manpowergroup.springboot.springboot3web.system.domain.model.role.Role;
import com.manpowergroup.springboot.springboot3web.system.infrastructure.mapper.role.RoleMapper;
import com.manpowergroup.springboot.springboot3web.system.application.service.RoleAppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@Slf4j
public class RoleAppServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleAppService {

    private final PageUtil pageUtil;

    public RoleAppServiceImpl(PageUtil pageUtil) {
        this.pageUtil = pageUtil;
    }

    @Override
    public JoinPageResult<Role> pageRoles(
            PageRequest pageRequest,
            RoleQueryRequest query
    ) {

        final Page<Role> page =
                pageUtil.toPage(pageRequest == null ? new PageRequest() : pageRequest);

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
        log.info("[RoleAppService#createRole] start request={}", request);

        final var code = StringUtils.normalize(request.code()).toUpperCase(Locale.ROOT);
        final var name = StringUtils.normalize(request.name());
        final var status = request.status();
        final var sort = request.sort() == null ? 0 : request.sort();

        final var role = Role.builder()
                .code(code)
                .name(name)
                .sort(sort)
                .status(status)
                .build();

        // UNIQUE違反は GlobalExceptionHandler で一括処理
        log.info("[RoleAppService#createRole] request={}", request);
        baseMapper.insert(role);

        log.info("[RoleAppService#createRole] success: entity={}", role);

        return role.getId();
    }

    @Override
    @Transactional
    public void updateRole(Long id, RoleSaveOrUpdateRequest request) {

        if (id == null) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "ロールIDが指定されていません。");
        }

        final var existing = getRoleById(id);

        final var code = StringUtils.normalize(request.code()).toUpperCase(Locale.ROOT);
        final var name = StringUtils.normalize(request.name());
        final var status = request.status();
        final var sort = request.sort() == null ? 0 : request.sort();

        existing.setCode(code)
                .setName(name)
                .setStatus(status)
                .setSort(sort);

        baseMapper.updateById(existing);

        log.info("ロールを更新しました。entity={}", existing);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        if (id == null) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "ロールIDが指定されていません。");
        }

        final var existing = getRoleById(id);

        final var affected = baseMapper.deleteById(id);
        if (affected == 0) {
            throw BizException.withDetail(ErrorCode.NOT_FOUND, "ロールが存在しません。id=" + id);
        }

        log.info(
                "ロールを削除しました。id={}, code={}, name={}",
                existing.getId(),
                existing.getCode(),
                existing.getName()
        );
    }

    @Override
    @Transactional
    public void changeStatus(Long id, Status status) {
        if (id == null) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "ロールIDが指定されていません。");
        }

        final var role = getRoleById(id);


        final var oldStatus = role.getStatus();
        if (oldStatus == status) {
            return;
        }

        role.setStatus(status);
        baseMapper.updateById(role);

        log.info(
                "ROLE_STATUS_CHANGED id={}, code={}, oldStatus={}, newStatus={}",
                role.getId(),
                role.getCode(),
                oldStatus,
                status
        );
    }


}
