package com.manpowergroup.springboot.springboot3web.system.infrastructure.repository;

import com.manpowergroup.springboot.springboot3web.system.domain.repository.PermissionRepository;
import com.manpowergroup.springboot.springboot3web.system.infrastructure.mapper.permission.PermissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MybatisPermissionRepositoryImpl implements PermissionRepository {

    private final PermissionMapper permissionMapper;

    @Override
    public List<String> selectPermissionCodesByUserId(Long userId) {
        return permissionMapper.selectPermissionCodesByUserId(userId);
    }
}