package com.manpowergroup.springboot.springboot3web.system.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "ロール一覧検索リクエスト")
public record RoleQueryRequest(

        @Schema(
                description = "検索キーワード（ロール名またはロールコードに対する部分一致）",
                example = "ADMIN"
        )
        String keyword,

        @Schema(
                description = "状態（0=無効、1=有効）",
                example = "1"
        )
        Byte status
) {}
