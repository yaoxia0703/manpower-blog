package com.manpowergroup.springboot.springboot3web.blog.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum MenuType {

    DIRECTORY((byte) 1),
    MENU((byte) 2),
    BUTTON((byte) 3);

    @EnumValue
    private final byte code;

    MenuType(byte code) {
        this.code = code;
    }

    @JsonValue
    public int toJson() {
        return code;
    }

    @JsonCreator
    public static MenuType fromJson(int code) {
        final byte b = (byte) code;
        return Arrays.stream(values())
                .filter(v -> v.code == b)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("無効なメニュー種別: " + code));
    }
}