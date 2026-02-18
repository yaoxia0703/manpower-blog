package com.manpowergroup.springboot.springboot3web.system.dto.role;

public record RoleQueryRequest(
        String keyword,
        Byte status
) {}
