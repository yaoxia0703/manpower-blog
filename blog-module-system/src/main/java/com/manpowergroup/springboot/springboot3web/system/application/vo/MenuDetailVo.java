package com.manpowergroup.springboot.springboot3web.system.application.vo;

import com.manpowergroup.springboot.springboot3web.blog.common.enums.MenuType;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "メニュー詳細")
public record MenuDetailVo(

        @Schema(description = "ID")
        Long id,

        @Schema(description = "親ID")
        Long parentId,

        @Schema(description = "メニュー名")
        String name,

        @Schema(description = "パス")
        String path,

        @Schema(description = "コンポーネント")
        String component,

        @Schema(description = "メニュー種別")
        MenuType type,

        @Schema(description = "表示順")
        Integer sort,

        @Schema(description = "状態")
        Status status

) {}