package com.manpowergroup.springboot.springboot3web.admin;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.JoinPageResult;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.PageRequest;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.Result;
import com.manpowergroup.springboot.springboot3web.system.application.dto.role.RoleQueryRequest;
import com.manpowergroup.springboot.springboot3web.system.application.dto.role.RoleSaveOrUpdateRequest;
import com.manpowergroup.springboot.springboot3web.system.domain.model.role.Role;
import com.manpowergroup.springboot.springboot3web.system.application.service.RoleAppService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * ロール関連のAPIコントローラー
 * </p>
 *
 * @author YAOXIA
 * @since 2026-03-01
 */
@RestController
@RequestMapping("/api/system/role")
@AllArgsConstructor
public class RoleController {

    private final RoleAppService roleService;

    @PreAuthorize("hasAuthority('sys:role:list')")
    @GetMapping("/pageList")
    public Result<JoinPageResult<Role>> page(PageRequest pageRequest, RoleQueryRequest query) {
        return Result.ok(roleService.pageRoles(pageRequest, query));
    }

    @PreAuthorize("hasAuthority('sys:role:detail')")
    @GetMapping("/{id}")
    public Result<Role> detail(@PathVariable @NotNull(message = "ロールIDは必須です") Long id) {
        return Result.ok(roleService.getRoleById(id));
    }

    @PreAuthorize("hasAuthority('sys:role:create')")
    @PostMapping
    public Result<Long> create(@RequestBody @Valid RoleSaveOrUpdateRequest request) {
        return Result.ok(roleService.createRole(request));
    }

    @PreAuthorize("hasAuthority('sys:role:update')")
    @PutMapping("/{id}")
    public Result<Void> update(
            @PathVariable @NotNull(message = "ロールIDは必須です") Long id,
            @RequestBody @Valid RoleSaveOrUpdateRequest request
    ) {
        roleService.updateRole(id, request);
        return Result.ok(null);
    }

    @PreAuthorize("hasAuthority('sys:role:delete')")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable @NotNull(message = "ロールIDは必須です") Long id) {
        roleService.deleteRole(id);
        return Result.ok(null);
    }

    @PreAuthorize("hasAuthority('sys:role:changeStatus')")
    @PatchMapping("/{id}/status")
    public Result<Void> changeStatus(
            @PathVariable @NotNull(message = "ロールIDは必須です") Long id,
            @RequestBody @Valid RoleSaveOrUpdateRequest request
    ) {
        roleService.changeStatus(id, request.status());
        return Result.ok(null);
    }
}
