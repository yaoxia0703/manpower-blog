package com.manpowergroup.springboot.springboot3web.blog.common.dto;

import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import org.slf4j.MDC;

/** 企业通用：带 code/message/data/traceId/timestamp */
public record Result<T>(
        Integer code,
        String message,
        T data,
        String traceId,
        Long timestamp
) {
    private static String trace() { return MDC.get("traceId"); }
    private static long now() { return System.currentTimeMillis(); }

    public static <T> Result<T> ok(T data) {
        return new Result<>(ErrorCode.SUCCESS.code(), ErrorCode.SUCCESS.message(), data, trace(), now());
    }

    public static <T> Result<T> ok() {
        return new Result<>(ErrorCode.SUCCESS.code(), ErrorCode.SUCCESS.message(), null, trace(), now());
    }

    public static <T> Result<T> error(ErrorCode code, String message) {
        return new Result<>(code.code(), message, null, trace(), now());
    }

    public static <T> Result<T> error(ErrorCode code) {
        return new Result<>(code.code(), code.message(), null, trace(), now());
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null, trace(), now());
    }
}
