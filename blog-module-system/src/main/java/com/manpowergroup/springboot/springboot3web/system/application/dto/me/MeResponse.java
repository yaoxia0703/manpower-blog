package com.manpowergroup.springboot.springboot3web.system.application.dto.me;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginUser;
import com.manpowergroup.springboot.springboot3web.system.application.vo.MenuTreeVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * "Me" API のレスポンス DTO
 * ログインユーザの情報、メニュー、権限を返す
 */
@Data
@Schema(description = "Me API レスポンス")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class MeResponse {

    @Schema(description = "ログインユーザの情報")
    private LoginUser user;

    @Schema(description = "ユーザがアクセス可能なメニューのツリー構造")
    private List<MenuTreeVo> menus;

    @Schema(description = "ユーザが持つ権限コードの一覧")
    private List<String> permissions;
}