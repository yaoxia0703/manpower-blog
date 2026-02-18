package com.manpowergroup.springboot.springboot3web.system.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "ロール新規作成／更新リクエスト")
public record RoleSaveOrUpdateRequest(

        @Schema(
                description = "ロールコード（英数字・一意）",
                example = "ADMIN",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String code,

        @Schema(
                description = "ロール名",
                example = "管理者",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String name,

        @Schema(
                description = "表示順（小さい値ほど前に表示）",
                example = "1"
        )
        Integer sort,

        @Schema(
                description = "状態（0=無効、1=有効）",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Byte status

) {}
