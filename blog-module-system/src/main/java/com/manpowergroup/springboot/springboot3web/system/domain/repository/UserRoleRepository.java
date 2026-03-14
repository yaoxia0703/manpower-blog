package com.manpowergroup.springboot.springboot3web.system.domain.repository;

import com.manpowergroup.springboot.springboot3web.system.domain.model.user.UserRole;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface UserRoleRepository {

    List<UserRole> selectAllByUserIdIncludeDeleted(Long userId);

    int restoreRoles(Long userId, Collection<Long> roleIds, LocalDateTime now);

    int logicalDeleteRoles(Long userId, Collection<Long> roleIds, LocalDateTime now);
}
