package com.manpowergroup.springboot.springboot3web.admin.user;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.Result;
import com.manpowergroup.springboot.springboot3web.system.user.model.User;
import com.manpowergroup.springboot.springboot3web.system.user.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserQueryController {

    private final UserService userService;

    public UserQueryController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public Result<User> get(@PathVariable Long id) {
        return Result.ok(userService.getById(id));
    }
}
