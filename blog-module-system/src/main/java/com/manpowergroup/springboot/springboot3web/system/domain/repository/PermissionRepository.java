package com.manpowergroup.springboot.springboot3web.system.domain.repository;

import java.util.List;

public interface PermissionRepository {

    List<String> selectPermissionCodesByUserId(Long userId);

}
