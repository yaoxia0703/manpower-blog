package com.manpowergroup.springboot.springboot3web.system.infrastructure.repository;

import com.manpowergroup.springboot.springboot3web.system.domain.model.user.UserRole;
import com.manpowergroup.springboot.springboot3web.system.domain.repository.UserRoleRepository;
import com.manpowergroup.springboot.springboot3web.system.infrastructure.mapper.user.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MybatisUserRoleRepositoryImpl implements UserRoleRepository {
    private final UserRoleMapper userRoleMapper;

    @Override
    public List<UserRole> selectAllByUserIdIncludeDeleted(Long userId) {
        return userRoleMapper.selectAllByUserIdIncludeDeleted(userId);
    }

    @Override
    public int restoreRoles(Long userId, Collection<Long> roleIds, LocalDateTime now) {
        return userRoleMapper.restoreRoles(userId, roleIds, now);
    }

    @Override
    public int logicalDeleteRoles(Long userId, Collection<Long> roleIds, LocalDateTime now) {
        return userRoleMapper.logicalDeleteRoles(userId, roleIds, now);
    }
}
