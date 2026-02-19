package com.manpowergroup.springboot.springboot3web.blog.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Arrays;

@Schema(description = "HTTPメソッド（GET/POST/PUT/DELETE/PATCH）")
public enum HttpMethod {

    GET,
    POST,
    PUT,
    DELETE,
    PATCH;

    @JsonValue
    public String toJson() {
        return name(); // 常に大文字
    }

    @JsonCreator
    public static HttpMethod fromJson(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        final String upper = value.trim().toUpperCase();

        return Arrays.stream(values())
                .filter(v -> v.name().equals(upper))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("無効なHTTPメソッド: " + value));
    }
}
