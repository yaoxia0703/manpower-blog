package com.manpowergroup.springboot.springboot3web.blog.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * アカウント種別（DB保存: EMAIL / PHONE）
 */
@Getter
@Schema(description = "アカウント種別")
public enum AccountType {

    EMAIL("EMAIL"),
    PHONE("PHONE");

    @EnumValue
    private final String code;

    AccountType(String code) {
        this.code = code;
    }

}
