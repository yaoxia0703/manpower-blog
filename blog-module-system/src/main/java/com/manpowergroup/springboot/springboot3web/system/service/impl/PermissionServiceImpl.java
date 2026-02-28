package com.manpowergroup.springboot.springboot3web.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.JoinPageResult;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.PageRequest;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.Status;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.blog.common.util.PageUtil;
import com.manpowergroup.springboot.springboot3web.blog.common.util.StringUtils;
import com.manpowergroup.springboot.springboot3web.system.assembler.PermissionAssembler;
import com.manpowergroup.springboot.springboot3web.system.dto.permission.PermissionQueryRequest;
import com.manpowergroup.springboot.springboot3web.system.dto.permission.PermissionSaveOrUpdateRequest;
import com.manpowergroup.springboot.springboot3web.system.dto.permission.PermissionStatusUpdateRequest;
import com.manpowergroup.springboot.springboot3web.system.entity.Permission;
import com.manpowergroup.springboot.springboot3web.system.mapper.PermissionMapper;
import com.manpowergroup.springboot.springboot3web.system.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 権限マスタ（MENU/BUTTON/API） 服务实现类
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Service
@AllArgsConstructor
@Slf4j
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    private final PermissionMapper permissionMapper;
    private final PageUtil pageUtil;

    @Override
    public List<String> selectPermissionCodesByUserId(Long userId) {
        return permissionMapper.selectPermissionCodesByUserId(userId);
    }


    @Override
    public JoinPageResult<Permission> pagePermission(PermissionQueryRequest queryRequest, PageRequest pageRequest) {
        final Page<Permission> page =
                pageUtil.toPage(pageRequest == null ? new PageRequest() : pageRequest);

        final var qw = new LambdaQueryWrapper<Permission>()
                .orderByAsc(Permission::getSort)
                .orderByDesc(Permission::getId);
        if (queryRequest != null) {
            final var keyword = StringUtils.normalize(queryRequest.keyword());
            qw.and(keyword != null, w ->
                            w.like(Permission::getCode, keyword)
                                    .or()
                                    .like(Permission::getName, keyword)
                    )
                    .eq(queryRequest.status() != null, Permission::getStatus, queryRequest.status());
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
    public Permission getPermissionById(Long id) {
        if (id == null) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "ロールIDが指定されていません。");
        }
        final var Permission = baseMapper.selectById(id);
        if (Permission == null) {
            throw BizException.withDetail(ErrorCode.NOT_FOUND, "ロールが存在しません。id=" + id);
        }
        return Permission;
    }

    @Override
    @Transactional
    public Long createPermission(PermissionSaveOrUpdateRequest request) {
        final var entity = PermissionAssembler.toNewEntity(request);
        baseMapper.insert(entity);
        log.info("権限を登録しました。entity={}", entity);
        return entity.getId();
    }

    @Override
    @Transactional
    public void updatePermission(Long id, PermissionSaveOrUpdateRequest request) {
        final var existing = getPermissionById(id);
        PermissionAssembler.applyToExisting(request, existing);
        baseMapper.updateById(existing);
        log.info("権限を更新しました。entity={}", existing);

    }

    @Override
    @Transactional
    public void deletePermission(Long id) {

        if (id == null) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "権限IDが指定されていません。");
        }

        final var existing = getPermissionById(id); // ✅ 先确保存在（建议你也封装一个方法）

        final var affected = baseMapper.deleteById(id);
        if (affected == 0) {
            throw BizException.withDetail(ErrorCode.NOT_FOUND, "権限が存在しません。id=" + id);
        }

        log.info(
                "権限を削除しました。id={}, code={}, name={}",
                existing.getId(),
                existing.getCode(),
                existing.getName()
        );
    }



    @Override
    @Transactional
    public void changeStatus(Long id, Status status) {

        if (id == null) {
            throw BizException.withDetail(ErrorCode.BAD_REQUEST, "権限IDが指定されていません。");
        }

        final var existing = baseMapper.selectById(id);

        if (existing == null) {
            throw BizException.withDetail(ErrorCode.NOT_FOUND, "権限が存在しません。id=" + id);
        }

        final var oldStatus = existing.getStatus();
        if (oldStatus == status) {
            return;
        }

        existing.setStatus(status);
        baseMapper.updateById(existing);

        log.info("権限のステータスを変更しました。id={}, code={}, name={}, {} -> {}",
                existing.getId(),
                existing.getCode(),
                existing.getName(),
                oldStatus,
                status);
    }

}
