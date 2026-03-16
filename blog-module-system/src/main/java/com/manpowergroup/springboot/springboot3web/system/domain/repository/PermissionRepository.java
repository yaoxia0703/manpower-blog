package com.manpowergroup.springboot.springboot3web.system.domain.repository;

import java.util.List;

public interface PermissionRepository {

    /**
     * 指定ユーザーに紐づく権限コード一覧を取得する
     *
     * @param userId ユーザーID
     * @return 権限コード一覧
     */
    List<String> selectPermissionCodesByUserId(Long userId);

}
