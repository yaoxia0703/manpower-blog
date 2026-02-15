package com.manpowergroup.springboot.springboot3web.blog.common.dto;

import com.manpowergroup.springboot.springboot3web.blog.common.enums.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "ログインリクエスト")
public class LoginRequest {

    @NotNull(message = "アカウント種別は必須です")
    @Schema(description = "アカウント種別（EMAIL / PHONE）", example = "EMAIL")
    private AccountType accountType;

    @NotBlank(message = "ログイン識別子は必須です")
    @Size(min = 8, max = 100, message = "ログイン識別子は8文字以上100文字以下でなければなりません")
    @Schema(description = "ログイン識別子（メールアドレス／電話番号）", example = "test@example.com")
    private String accountValue;

    @NotBlank(message = "パスワードは必須です")
    @Size(min = 8, max = 16, message = "パスワードは8文字以上16文字以下でなければなりません")
    @Schema(description = "パスワード（平文）", example = "Passw0rd!")
    private String password;
}
