package com.manpowergroup.springboot.springboot3web.blog.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Arrays;

@Getter
@Schema(description = "認証状態（0=未認証、1=認証済み）")
public enum VerifiedStatus {

    UNVERIFIED((byte) 0),
    VERIFIED((byte) 1);

    @EnumValue
    private final byte code;

    VerifiedStatus(byte code) {
        this.code = code;
    }

    /**
     * JSON 返却は 0/1 にする（"VERIFIED" ではなく数値）
     */
    @JsonValue
    public int toJson() {
        return code;
    }

    /**
     * JSON 入力は 0/1 を許可
     */
    @JsonCreator
    public static VerifiedStatus fromJson(int code) {
        final byte b = (byte) code;
        return Arrays.stream(values())
                .filter(v -> v.code == b)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("無効な認証状態: " + code));
    }
}
