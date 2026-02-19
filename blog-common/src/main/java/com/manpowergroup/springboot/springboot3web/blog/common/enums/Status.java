package com.manpowergroup.springboot.springboot3web.blog.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Status {

    DISABLED((byte) 0),
    ENABLED((byte) 1);

    @EnumValue
    private final byte code;

    Status(byte code) {
        this.code = code;
    }

    @JsonValue
    public int toJson() {
        return code;
    }

    @JsonCreator
    public static Status fromJson(int code) {
        final byte b = (byte) code;
        return Arrays.stream(values())
                .filter(v -> v.code == b)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("無効な状態: " + code));
    }
}
