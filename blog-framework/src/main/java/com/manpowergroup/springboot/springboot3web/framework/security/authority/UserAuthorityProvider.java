package com.manpowergroup.springboot.springboot3web.framework.security.authority;

import java.util.List;

public interface UserAuthorityProvider {

    /**
     * userId から権限コード一覧（permission codes）を取得する
     * 例: ["sys:user:list", "sys:user:btn:add"]
     */
    List<String> loadPermissionCodes(Long userId);


}
