package com.manpowergroup.springboot.springboot3web.system.dto.role;

import com.manpowergroup.springboot.springboot3web.blog.common.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "ロール新規作成／更新リクエスト")
public record RoleSaveOrUpdateRequest(

        @Schema(
                description = "ロールコード（英数字・一意）",
                example = "ADMIN"
        )
        @NotBlank(message = "ロールコードは必須です")
        @Pattern(
                regexp = "^[A-Za-z0-9]+$",
                message = "ロールコードは英数字のみ使用できます"
        )
        String code,

        @Schema(
                description = "ロール名",
                example = "管理者"
        )
        @NotBlank(message = "ロール名は必須です")
        String name,

        @Schema(
                description = "表示順（小さい値ほど前に表示）",
                example = "1"
        )
        @NotNull(message = "表示順は必須です")
        @Min(value = 0, message = "表示順は0以上で入力してください")
        Integer sort,

        @Schema(
                description = "状態（0=無効、1=有効）",
                example = "1"
        )
        @NotNull(message = "状態は必須です")
        Status status

) {}