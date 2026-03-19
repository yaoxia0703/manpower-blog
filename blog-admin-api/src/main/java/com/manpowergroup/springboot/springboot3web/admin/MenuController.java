package com.manpowergroup.springboot.springboot3web.admin;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.Result;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.system.application.dto.menu.MenuSaveOrUpdateRequest;
import com.manpowergroup.springboot.springboot3web.system.application.dto.menu.MenuStatusUpdateRequest;
import com.manpowergroup.springboot.springboot3web.system.application.service.MenuAppService;
import com.manpowergroup.springboot.springboot3web.system.application.vo.MenuTreeVo;
import com.manpowergroup.springboot.springboot3web.system.domain.model.menu.Menu;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/system/menu")
public class MenuController {

    private final MenuAppService menuAppService;

    @PreAuthorize("hasAuthority('sys:menu:list')")
    @GetMapping
    public Result<List<MenuTreeVo>> list() {
        log.info("[MenuController#list] request received");
        return Result.ok(menuAppService.selectAllMenus());
    }

    @PreAuthorize("hasAuthority('sys:menu:detail')")
    @GetMapping("/{id}")
    public Result<Menu> detail(@PathVariable @NotNull(message = "メニューIDは必須です") Long id) {
        log.info("[MenuController#detail] request received: id={}", id);
        Menu menu = menuAppService.getById(id);
        if (menu == null) {
            throw BizException.withDetail(ErrorCode.NOT_FOUND, "メニューが存在しません");
        }
        return Result.ok(menu);
    }

    @PreAuthorize("hasAuthority('sys:menu:create')")
    @PostMapping
    public Result<Long> create(@RequestBody @Valid MenuSaveOrUpdateRequest request) {
        log.info("[MenuController#create] request received: request={}", request);
        return Result.ok(menuAppService.createMenu(request));
    }

    @PreAuthorize("hasAuthority('sys:menu:update')")
    @PutMapping("/{id}")
    public Result<Void> update(
            @PathVariable @NotNull(message = "メニューIDは必須です") Long id,
            @RequestBody @Valid MenuSaveOrUpdateRequest request
    ) {
        log.info("[MenuController#update] request received: id={}, request={}", id, request);
        menuAppService.updateMenu(id, request);
        return Result.ok(null);
    }

    @PreAuthorize("hasAuthority('sys:menu:delete')")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable @NotNull(message = "メニューIDは必須です") Long id) {
        log.info("[MenuController#delete] request received: id={}", id);
        menuAppService.deleteMenu(id);
        return Result.ok(null);
    }

    @PreAuthorize("hasAuthority('sys:menu:changeStatus')")
    @PatchMapping("/{id}/status")
    public Result<Void> changeStatus(
            @PathVariable @NotNull(message = "メニューIDは必須です") Long id,
            @RequestBody @Valid MenuStatusUpdateRequest request
    ) {
        log.info("[MenuController#changeStatus] request received: id={}, status={}", id, request.status());
        menuAppService.changeStatus(id, request.status());
        return Result.ok(null);
    }
}
