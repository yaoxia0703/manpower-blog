package com.manpowergroup.springboot.springboot3web.system.dto.permission;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "権限状態変更リクエスト")
public record PermissionStatusUpdateRequest(

        @Schema(
                description = "状態（0=無効、1=有効）",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Byte status

) {}
