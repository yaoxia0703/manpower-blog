package com.manpowergroup.springboot.springboot3web.blog.common.dto;

import com.manpowergroup.springboot.springboot3web.blog.common.enums.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "ログイン成功レスポンス（セッション保持）")
public class LoginUser  {

    @Schema(description = "ユーザーID（t_sys_user.id）")
    private Long userId;

    @Schema(description = "ユーザーID（t_sys_user_account.id）")
    private Long accountId;

    @Schema(description = "ユーザー氏名（t_sys_user.nick_name）")
    private String nickName;

    @Schema(description = "アカウント種別（EMAIL / PHONE）")
    private AccountType accountType;

    @Schema(description = "ログイン識別子")
    private String accountValue;

    @Schema(description = "ロール一覧")
    private List<String> roleNames;




}
