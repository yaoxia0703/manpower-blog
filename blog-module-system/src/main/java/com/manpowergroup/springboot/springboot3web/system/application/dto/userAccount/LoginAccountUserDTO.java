package com.manpowergroup.springboot.springboot3web.system.application.dto.userAccount;

import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.Status;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.VerifiedStatus;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import com.manpowergroup.springboot.springboot3web.system.domain.model.user.User;
import com.manpowergroup.springboot.springboot3web.system.domain.model.user.UserAccount;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * ログイン時のアカウント＋ユーザー情報取得用DTO
 * ログイン処理において、アカウントとユーザーをJOINした結果をマッピングするためのオブジェクト
 */
@Data
@Schema(description = "ログイン用アカウント・ユーザー統合情報")
public class LoginAccountUserDTO {

    @Schema(description = "ユーザーID")
    private Long userId;

    @Schema(description = "アカウントID")
    private Long accountId;

    @Schema(description = "ユーザー表示名")
    private String nickName;

    @Schema(description = "アカウント種別（EMAIL / PHONE / USERNAME 等）")
    private String accountType;

    @Schema(description = "アカウント識別値（メールアドレス・電話番号など）")
    private String accountValue;

    @Schema(description = "ユーザーステータス（1=有効、0=無効）")
    private Status userStatus;

    @Schema(description = "アカウントステータス（1=有効、0=無効）")
    private Status accountStatus;

    @Schema(description = "認証状態（1=認証済み、0=未認証）")
    private VerifiedStatus verified;

    @Schema(
            description = "暗号化パスワード（ログイン照合専用・APIレスポンスには出力しない）",
            accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private String password;

}
