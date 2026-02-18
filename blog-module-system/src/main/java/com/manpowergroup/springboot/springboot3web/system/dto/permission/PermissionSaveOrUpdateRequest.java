package com.manpowergroup.springboot.springboot3web.system.dto.permission;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "権限の新規作成／更新リクエスト")
public record PermissionSaveOrUpdateRequest(

        @Schema(
                description = "親権限ID（ルートの場合は null）",
                example = "0"
        )
        Long parentId,

        @Schema(
                description = "権限名",
                example = "ユーザー管理"
        )
        String name,

        @Schema(
                description = "権限制御コード（例：user:add、article:edit）",
                example = "user:add"
        )
        String code,

        @Schema(
                description = "権限種別（1=MENU、2=BUTTON、3=API）",
                example = "1"
        )
        Byte type,

        @Schema(
                description = "対象パス（MENU または API の場合に設定）",
                example = "/system/user"
        )
        String path,

        @Schema(
                description = "HTTPメソッド（API の場合：GET / POST / PUT / DELETE）",
                example = "GET"
        )
        String method,

        @Schema(
                description = "表示順（小さい値ほど前に表示）",
                example = "1"
        )
        Integer sort,

        @Schema(
                description = "状態（0=無効、1=有効）",
                example = "1"
        )
        Byte status
) {
}
