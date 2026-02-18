package com.manpowergroup.springboot.springboot3web.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.JoinPageResult;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.PageRequest;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.Result;
import com.manpowergroup.springboot.springboot3web.system.dto.role.RoleQueryRequest;
import com.manpowergroup.springboot.springboot3web.system.dto.role.RoleSaveOrUpdateRequest;
import com.manpowergroup.springboot.springboot3web.system.entity.Role;
import com.manpowergroup.springboot.springboot3web.system.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/system/role")
@AllArgsConstructor
public class RoleController {

    private final  RoleService roleService;

    @PreAuthorize("hasAuthority('sys:role:pageList')")
    @GetMapping("/pageList")
    public Result<JoinPageResult<Role>> page(PageRequest pageRequest, RoleQueryRequest query) {
        return Result.ok(roleService.pageRoles(pageRequest, query));
    }

    @GetMapping("/{id}")
    public Result<Role> detail(@PathVariable Long id) {
        return Result.ok(roleService.getRoleById(id));
    }

    @PostMapping
    public Result<Long> create(@RequestBody RoleSaveOrUpdateRequest request) {
        return Result.ok(roleService.createRole(request));
    }
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody RoleSaveOrUpdateRequest request) {
        roleService.updateRole(id, request);
        return Result.ok(null);
    }
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.deleteRole(id);
        return Result.ok(null);
    }

    @PatchMapping("/{id}/status")
    public Result<Void> changeStatus(@PathVariable Long id, @RequestBody RoleSaveOrUpdateRequest request) {
        roleService.changeStatus(id, request.status());
        return Result.ok(null);
    }
}
