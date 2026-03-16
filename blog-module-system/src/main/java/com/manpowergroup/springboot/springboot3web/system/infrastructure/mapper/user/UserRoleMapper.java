package com.manpowergroup.springboot.springboot3web.system.infrastructure.mapper.user;

import com.manpowergroup.springboot.springboot3web.system.domain.model.user.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * ユーザー・ロール紐付け Mapper 接口
 * </p>
 *
 * @author YAOXIA
 * @since 2025-12-18
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 指定ユーザーに紐づくロール関連を取得する（論理削除データを含む）
     *
     * @param userId ユーザーID
     * @return ユーザーロール関連一覧
     */
    List<UserRole> selectAllByUserIdIncludeDeleted(@Param("userId") Long userId);

    /**
     * 論理削除されたユーザーロール関連を復元する
     *
     * @param userId ユーザーID
     * @param roleIds ロールID一覧
     * @param now 更新日時
     * @return 更新件数
     */
    int restoreRoles(@Param("userId") Long userId,
                     @Param("roleIds") Collection<Long> roleIds,
                     @Param("now") LocalDateTime now);

    /**
     * 指定ユーザーに紐づくロール関連を論理削除する
     *
     * @param userId ユーザーID
     * @param roleIds ロールID一覧
     * @param now 更新日時
     * @return 更新件数
     */
    int logicalDeleteRoles(
            @Param("userId") Long userId,
            @Param("roleIds") Collection<Long> roleIds,
            @Param("now") LocalDateTime now
    );
}
