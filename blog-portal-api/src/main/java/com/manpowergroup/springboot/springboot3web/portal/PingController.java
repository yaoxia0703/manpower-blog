package com.manpowergroup.springboot.springboot3web.portal;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {
    @GetMapping("/api/portal/ping")
    public Result<String> ping() {
        return Result.ok("pong");
    }
}
