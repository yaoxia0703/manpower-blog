package com.manpowergroup.springboot.springboot3web.system.dto.permission;

import com.manpowergroup.springboot.springboot3web.blog.common.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "権限状態変更リクエスト")
public record PermissionStatusUpdateRequest(

        @Schema(
                description = "状態（0=無効、1=有効）",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "状態は必須です")
        Status status

) {}
