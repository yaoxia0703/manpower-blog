package com.manpowergroup.springboot.springboot3web.system.application.dto.menu;

import com.manpowergroup.springboot.springboot3web.blog.common.enums.MenuType;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "メニュー保存または更新リクエスト")
public record MenuSaveOrUpdateRequest(

        @Schema(description = "親メニューID（トップレベルの場合は0）", example = "0")
        @NotNull
        @Min(value = 0, message = "親メニューIDは0以上でなければなりません")
        Long parentId,

        @Schema(description = "メニュー名称", example = "ユーザ管理")
        @NotNull(message = "メニュー名称は必須です")
        @Size(max = 100, message = "メニュー名称は100文字以内で入力してください")
        String name,

        @Schema(description = "フロントエンドのルートパス", example = "/system/user")
        @Size(max = 200, message = "ルートパスは200文字以内で入力してください")
        String path,

        @Schema(description = "フロントエンドのコンポーネントパス", example = "system/user/index")
        @Size(max = 200, message = "コンポーネントパスは200文字以内で入力してください")
        String component,

        @Schema(description = "権限識別子", example = "sys:user:list")
        @Size(max = 100, message = "権限識別子は100文字以内で入力してください")
        String permission,

        @Schema(description = "メニュー種別（1=ディレクトリ、2=メニュー、3=ボタン）", example = "2")
        @NotNull(message = "メニュー種別は必須です")
        MenuType type,

        @Schema(description = "表示順", example = "1")
        @NotNull(message = "表示順は必須です")
        @Min(value = 0)
        Integer sort,

        @Schema(description = "アイコン", example = "el-icon-user")
        @Size(max = 100, message = "アイコンは100文字以内で入力してください")
        String icon,

        @Schema(description = "状態（0=無効、1=有効）", example = "1")
        @NotNull(message = "状態は必須です")
        Status status

) {}
