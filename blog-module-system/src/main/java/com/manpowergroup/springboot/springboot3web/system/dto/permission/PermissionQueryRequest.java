package com.manpowergroup.springboot.springboot3web.system.dto.permission;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "権限一覧検索リクエスト")
public record PermissionQueryRequest(

        @Schema(
                description = "検索キーワード（権限名または権限制御コードに対する部分一致）",
                example = "user"
        )
        String keyword,

        @Schema(
                description = "権限種別（1=MENU、2=BUTTON、3=API）",
                example = "1"
        )
        Byte type,

        @Schema(
                description = "HTTPメソッド（API の場合：GET / POST / PUT / DELETE）",
                example = "GET"
        )
        String method,

        @Schema(
                description = "状態（0=無効、1=有効）",
                example = "1"
        )
        Byte status
) {
}
