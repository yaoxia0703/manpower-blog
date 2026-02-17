package com.manpowergroup.springboot.springboot3web.system.security;

import com.manpowergroup.springboot.springboot3web.blog.common.util.CollectionUtils;
import com.manpowergroup.springboot.springboot3web.framework.security.authority.UserAuthorityProvider;
import com.manpowergroup.springboot.springboot3web.system.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * framework 側の UserAuthorityProvider の system 実装
 * userId から permission codes をロードする（RBAC -> Permission）
 */
@Service
public class SystemUserAuthorityProvider implements UserAuthorityProvider {

    private final PermissionService permissionService;

    public SystemUserAuthorityProvider(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    public List<String> loadPermissionCodes(Long userId) {
        Objects.requireNonNull(userId, "userId is null");

        return CollectionUtils.safeList(
                permissionService.selectPermissionCodesByUserId(userId)
        );
    }
}
