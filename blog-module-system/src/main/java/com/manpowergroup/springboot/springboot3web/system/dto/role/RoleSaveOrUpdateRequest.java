package com.manpowergroup.springboot.springboot3web.system.dto.role;

public record RoleSaveOrUpdateRequest(
        String code,
        String name,
        Integer sort,
        Byte status
) {}
