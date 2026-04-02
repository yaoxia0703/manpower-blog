package com.manpowergroup.springboot.springboot3web.system.application.vo;

import com.manpowergroup.springboot.springboot3web.blog.common.enums.MenuType;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "メニューツリー")
public class MenuTreeVo {

    @Schema(description = "メニューID")
    private Long id;

    @Schema(description = "親メニューID")
    private Long parentId;

    @Schema(description = "メニュー名称")
    private String name;

    @Schema(description = "ルートパス")
    private String path;

    @Schema(description = "コンポーネントパス")
    private String component;

    @Schema(description = "アイコン")
    private String icon;

    @Schema(description = "メニュー種別（1=ディレクトリ、2=メニュー、3=ボタン）", example = "1")
    private MenuType type;

    @Schema(description = "表示順")
    private Integer sort;

    @Schema(description = "状態（0=無効、1=有効）", example = "1")
    private Status status;

    @Schema(description = "权限标识符")
    private String permission;

    @Schema(description = "子メニュー")
    private List<MenuTreeVo> children = new ArrayList<>();


}