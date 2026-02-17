package com.manpowergroup.springboot.springboot3web.blog.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "ログイン成功レスポンス（JWT）")
public class LoginResponse<T> {

    @Schema(description = "アクセストークン（Bearer）")
    private String accessToken;

    @Schema(description = "ログインユーザー情報（クライアント別）")
    private T user;
}
