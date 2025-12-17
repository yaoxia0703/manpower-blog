package com.manpowergroup.springboot.springboot3web.blog.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;
import java.util.Objects;

/**
 * 統一レスポンス構造（旧仕様との互換、新規追加：traceId / timestamp）
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

    /** 新規追加：フロントエンド／ログのトラブルシュート用に関連付け */
    @JsonProperty("traceId")
    private String traceId;

    /** 新規追加：サーバ側で生成された時刻（エポックミリ秒） */
    @JsonProperty("timestamp")
    private Long timestamp;

    @JsonProperty("detail")
    private String detail; // 追加：詳細メッセージ（i18n key の翻訳や例外の具体理由を格納）

    /* -------------------- 基本ファクトリーメソッド：従来の意味を維持 -------------------- */

    public Result<T> withDetail(String detail) {
        this.detail = detail;
        if (this.timestamp == null) this.timestamp = now();
        return this;
    }


    public static <T> Result<T> ok(T data, String msg) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMessage(msg);
        r.setData(data);
        r.setTimestamp(now());
        return r;
    }



    public static <T> Result<T> ok(T data) {
        return ok(data, "success.ok"); // i18nキーを推奨；例外ハンドラ側で翻訳される
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

    public static <T> Result<T> errorWithDetail(int code, String msg, String detail) {
        return Result.<T>error(code, msg).withDetail(detail);
    }


    public static <T> Result<T> error(int code, String msg) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMessage(msg);
        r.setTimestamp(now());
        return r;
    }

    /** 以前の呼び方との互換：fail(...) */
    public static <T> Result<T> fail(int code, String msg) {
        return error(code, msg);
    }

    /* -------------------- ErrorCode 列挙との連携用オーバーロード（任意） -------------------- */

    /**
     * 推奨する ErrorCode の設計：
     * int code(); String key();
     * key は i18n メッセージコード（例: "error.validation"）
     */
    public static <T> Result<T> error(EnumLikeErrorCode ec) {
        return error(ec.code(), ec.key());
    }

    public static <T> Result<T> error(EnumLikeErrorCode ec, String overrideMsgOrKey) {
        return error(ec.code(), overrideMsgOrKey);
    }

    public static <T> Result<T> ok(EnumLikeErrorCode ec, T data) {
        // シナリオ：成功だが文言提示が必要なケース
        Result<T> r = new Result<>();
        r.setCode(ec.code());
        r.setMessage(ec.key());
        r.setData(data);
        r.setTimestamp(now());
        return r;
    }

    /* -------------------- traceId 補助（例外ハンドラ／インターセプタで設定） -------------------- */

    public Result<T> withTraceId(String traceId) {
        this.traceId = traceId;
        // 外部で timestamp 未設定の場合はここで補完
        if (this.timestamp == null) this.timestamp = now();
        return this;
    }

    /* -------------------- 汎用ファクトリーメソッド -------------------- */

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

    /* -------------------- 互換用インタフェース：ErrorCode 適用 -------------------- */

    /**
     * ErrorCode 列挙に強依存しないために最小インタフェースを定義。
     * 独自の列挙型でこのインタフェースを実装してもよいし、
     * 不要なら削除して上記のオーバーロードを直接自作の列挙に変更してもよい。
     */
    public interface EnumLikeErrorCode {
        int code();

        /** i18n メッセージコード（例: "error.validation"）を返す */
        String key();

        default String messageOrKey() {
            return Objects.requireNonNullElse(key(), "");
        }
    }
}
