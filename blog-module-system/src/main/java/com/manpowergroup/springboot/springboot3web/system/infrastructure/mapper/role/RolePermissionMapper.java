package com.manpowergroup.springboot.springboot3web.system.infrastructure.mapper.role;

import com.manpowergroup.springboot.springboot3web.system.domain.model.role.RolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * ロール・権限紐付け Mapper 接口
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    /**
     * 指定ロールに紐づく権限関連を取得する（論理削除データを含む）
     *
     * @param roleId ロールID
     * @return ロール権限関連一覧
     */
    List<RolePermission> selectAllByRoleIdIncludeDeleted(@Param("roleId") Long roleId);

    /**
     * 論理削除されたロール権限関連を復元する
     *
     * @param roleId ロールID
     * @param permissionIds 権限ID一覧
     * @param now 更新日時
     * @return 更新件数
     */
    int restorePermissions(@Param("roleId") Long roleId,
                           @Param("permissionIds") Collection<Long> permissionIds,
                           @Param("now") LocalDateTime now);

    /**
     * 指定ロールに紐づく権限関連を論理削除する
     *
     * @param roleId ロールID
     * @param permissionIds 権限ID一覧
     * @param now 更新日時
     * @return 更新件数
     */
    int logicalDeletePermissions(@Param("roleId") Long roleId,
                                 @Param("permissionIds") Collection<Long> permissionIds,
                                 @Param("now") LocalDateTime now);

}
