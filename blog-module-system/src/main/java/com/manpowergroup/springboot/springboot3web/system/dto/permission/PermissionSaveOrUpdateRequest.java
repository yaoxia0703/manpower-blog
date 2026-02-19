package com.manpowergroup.springboot.springboot3web.system.dto.permission;

import com.manpowergroup.springboot.springboot3web.blog.common.enums.HttpMethod;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.PermissionType;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.Status;
import com.manpowergroup.springboot.springboot3web.blog.common.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "権限の新規作成／更新リクエスト")
public record PermissionSaveOrUpdateRequest(

        @Schema(description = "親権限ID（トップレベルの場合は0）", example = "0")
        @NotNull(message = "親権限IDは必須です")
        @Min(value = 0, message = "親権限IDは0以上でなければなりません")
        Long parentId,

        @Schema(description = "権限名", example = "ユーザー管理")
        @NotBlank(message = "権限名は必須です")
        @Size(max = 100, message = "権限名は100文字以内で入力してください")
        String name,

        @Schema(description = "権限制御コード（例：user:add、article:edit）", example = "user:add")
        @NotBlank(message = "権限制御コードは必須です")
        @Size(max = 100, message = "権限制御コードは100文字以内で入力してください")
        String code,

        @Schema(description = "権限種別（1=MENU、2=BUTTON、3=API）", example = "1")
        @NotNull(message = "権限種別は必須です")
        PermissionType type,

        @Schema(description = "対象パス（MENU または API の場合に設定）", example = "/system/user")
        @Size(max = 255, message = "対象パスは255文字以内で入力してください")
        @Pattern(regexp = "^$|(/.*)", message = "対象パスは「/」から始まる形式で入力してください")
        String path,

        @Schema(description = "HTTPメソッド（API の場合：GET / POST / PUT / DELETE / PATCH）", example = "GET")
        HttpMethod method,

        @Schema(description = "表示順（小さい値ほど前に表示）", example = "1")
        @NotNull(message = "表示順は必須です")
        @Min(value = 0, message = "表示順は0以上でなければなりません")
        Integer sort,

        @Schema(description = "状態（0=無効、1=有効）", example = "1")
        @NotNull(message = "状態は必須です")
        Status status
) {

    @AssertTrue(message = "権限種別=MENUの場合、対象パス(path)は必須です")
    public boolean isMenuPathValid() {
        if (!isMenu()) return true;
        return StringUtils.hasText(path);
    }

    @AssertTrue(message = "権限種別=MENUの場合、HTTPメソッド(method)は指定できません")
    public boolean isMenuMethodEmpty() {
        if (!isMenu()) return true;
        return method == null;
    }

    @AssertTrue(message = "権限種別=APIの場合、対象パス(path)は必須です")
    public boolean isApiPathValid() {
        if (!isApi()) return true;
        return StringUtils.hasText(path);
    }

    @AssertTrue(message = "権限種別=APIの場合、HTTPメソッド(method)は必須です")
    public boolean isApiMethodValid() {
        if (!isApi()) return true;
        return method != null;
    }

    @AssertTrue(message = "権限種別=BUTTONの場合、対象パス(path)は指定できません")
    public boolean isButtonPathEmpty() {
        if (!isButton()) return true;
        return path == null || path.isBlank();
    }


    @AssertTrue(message = "権限種別=BUTTONの場合、HTTPメソッド(method)は指定できません")
    public boolean isButtonMethodEmpty() {
        if (!isButton()) return true;
        return method == null;
    }

    private boolean isMenu() {
        return type == PermissionType.MENU;
    }

    private boolean isButton() {
        return type == PermissionType.BUTTON;
    }

    private boolean isApi() {
        return type == PermissionType.API;
    }
}
