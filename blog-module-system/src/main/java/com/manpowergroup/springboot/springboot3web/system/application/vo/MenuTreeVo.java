package com.manpowergroup.springboot.springboot3web.system.application.vo;

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

    @Schema(description = "メニュー種別（1=ディレクトリ、2=メニュー、3=ボタン）")
    private Integer type;

    @Schema(description = "表示順")
    private Integer sort;

    @Schema(description = "状態")
    private Integer status;

    @Schema(description = "子メニュー")
    private List<MenuTreeVo> children = new ArrayList<>();
}