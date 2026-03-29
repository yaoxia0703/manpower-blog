package com.manpowergroup.springboot.springboot3web.admin;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.Result;
import com.manpowergroup.springboot.springboot3web.system.domain.model.user.User;
import com.manpowergroup.springboot.springboot3web.system.application.service.UserAppService;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * ユーザー関連のAPIコントローラー
 * </p>
 *
 * @author YAOXIA
 * @since 2026-03-01
 */
@RestController
@RequestMapping("/api/users")
public class UserQueryController {

    private final UserAppService userService;

    public UserQueryController(UserAppService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public Result<User> get(@PathVariable Long id) {
        return Result.ok(userService.getById(id));
    }
}
