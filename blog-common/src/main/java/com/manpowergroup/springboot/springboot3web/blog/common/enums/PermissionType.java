package com.manpowergroup.springboot.springboot3web.blog.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Arrays;

@Getter
@Schema(description = "権限種別（1=MENU、2=BUTTON、3=API）")
public enum PermissionType {

    MENU((byte) 1),
    BUTTON((byte) 2),
    API((byte) 3);

    @EnumValue
    private final byte code;

    PermissionType(byte code) {
        this.code = code;
    }

    /**
     * JSON 返却は 1/2/3 にする（"MENU" ではなく数値）
     */
    @JsonValue
    public int toJson() {
        return code;
    }

    /**
     * JSON 入力は 1/2/3 を許可
     */
    @JsonCreator
    public static PermissionType fromJson(int code) {
        final byte b = (byte) code;
        return Arrays.stream(values())
                .filter(v -> v.code == b)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("無効な権限種別: " + code));
    }
}
