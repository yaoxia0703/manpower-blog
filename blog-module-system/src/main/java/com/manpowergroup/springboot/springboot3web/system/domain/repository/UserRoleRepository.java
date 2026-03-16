package com.manpowergroup.springboot.springboot3web.system.domain.repository;

import com.manpowergroup.springboot.springboot3web.system.domain.model.user.UserRole;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface UserRoleRepository {

    /**
     * 指定ユーザーに紐づくロール関連を取得する（論理削除データを含む）
     *
     * @param userId ユーザーID
     * @return ユーザーロール関連一覧
     */
    List<UserRole> selectAllByUserIdIncludeDeleted(Long userId);

    /**
     * 論理削除されたユーザーロール関連を復元する
     *
     * @param userId ユーザーID
     * @param roleIds ロールID一覧
     * @param now 更新日時
     * @return 更新件数
     */
    int restoreRoles(Long userId, Collection<Long> roleIds, LocalDateTime now);

    /**
     * 指定ユーザーに紐づくロール関連を論理削除する
     *
     * @param userId ユーザーID
     * @param roleIds ロールID一覧
     * @param now 更新日時
     * @return 更新件数
     */
    int logicalDeleteRoles(Long userId, Collection<Long> roleIds, LocalDateTime now);
}
