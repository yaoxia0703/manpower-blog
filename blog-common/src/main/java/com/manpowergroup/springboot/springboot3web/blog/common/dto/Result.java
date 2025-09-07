package com.manpowergroup.springboot.springboot3web.blog.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;
import java.util.Objects;

/**
 * 统一返回结构（兼容旧用法，新增 traceId/timestamp）
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {

    @JsonProperty("code")
    private int code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private T data;

    /** 新增：便于前端/日志排错关联 */
    @JsonProperty("traceId")
    private String traceId;

    /** 新增：服务端生成时间（毫秒 epoch） */
    @JsonProperty("timestamp")
    private Long timestamp;

    /* -------------------- 基础工厂：保持原有语义 -------------------- */

    public static <T> Result<T> ok(T data, String msg) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMessage(msg);
        r.setData(data);
        r.setTimestamp(now());
        return r;
    }

    public static <T> Result<T> ok(T data) {
        return ok(data, "success.ok"); // 建议使用 i18n key；异常处理器也会做翻译
    }

    public static <T> Result<T> okMsg(String msg) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMessage(msg);
        r.setTimestamp(now());
        return r;
    }

    public static <T> Result<T> error(String msg) {
        return error(500, msg);
    }

    public static <T> Result<T> error(int code, String msg) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMessage(msg);
        r.setTimestamp(now());
        return r;
    }

    /** 兼容你以前可能有的写法：fail(...) */
    public static <T> Result<T> fail(int code, String msg) {
        return error(code, msg);
    }

    /* -------------------- 与 ErrorCode 枚举的友好重载（可选） -------------------- */

    /**
     * 建议你的 ErrorCode 设计为：int code(); String key();
     * 其中 key 是 i18n 消息码（如 "error.validation"）
     */
    public static <T> Result<T> error(EnumLikeErrorCode ec) {
        return error(ec.code(), ec.key());
    }

    public static <T> Result<T> error(EnumLikeErrorCode ec, String overrideMsgOrKey) {
        return error(ec.code(), overrideMsgOrKey);
    }

    public static <T> Result<T> ok(EnumLikeErrorCode ec, T data) {
        // 场景：某些“成功但需要文案”的响应
        Result<T> r = new Result<>();
        r.setCode(ec.code());
        r.setMessage(ec.key());
        r.setData(data);
        r.setTimestamp(now());
        return r;
    }

    /* -------------------- traceId 辅助（供异常处理器/拦截器设置） -------------------- */

    public Result<T> withTraceId(String traceId) {
        this.traceId = traceId;
        // 若外部未设置时间，这里补上
        if (this.timestamp == null) this.timestamp = now();
        return this;
    }

    /* -------------------- 通用工厂 -------------------- */

    public static <T> Result<T> of(int code, String msg, T data) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMessage(msg);
        r.setData(data);
        r.setTimestamp(now());
        return r;
    }

    private static long now() {
        return Instant.now().toEpochMilli();
    }

    /* -------------------- 兼容接口：可选的 ErrorCode 适配 -------------------- */

    /**
     * 为了不强依赖你的 ErrorCode 枚举定义，这里用一个最小接口做适配。
     * 你的枚举可实现此接口，或你也可以删除这段并把上面的重载改成你的枚举类型。
     */
    public interface EnumLikeErrorCode {
        int code();

        /** 返回 i18n 消息码（如 "error.validation"） */
        String key();

        default String messageOrKey() {
            return Objects.requireNonNullElse(key(), "");
        }
    }
}
