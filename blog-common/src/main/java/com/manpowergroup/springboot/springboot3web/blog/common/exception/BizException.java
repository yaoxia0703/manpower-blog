package com.manpowergroup.springboot.springboot3web.blog.common.exception;

import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import lombok.Getter;

/**
 * 業務例外：
 * - ErrorCode
 * - i18n messageKey + 任意のプレースホルダ引数
 * - （任意）開発/テスト向け detail（prod では返さない想定）
 */
@Getter
public class BizException extends RuntimeException {

    private final ErrorCode code;

    /** i18n 文言キー（例: "error.not_found" / "article.not_found" / "field.required"） */
    private final String messageKey;

    /** i18n プレースホルダ引数：properties 内の {0},{1}... に対応 */
    private final Object[] args;

    /**
     * 任意：開発/テスト向け補足情報（例: "userId=xxx, status=0"）
     * ※ GlobalExceptionHandler 側で prod の場合は返さない運用推奨
     */
    private final String detail;

    /* ===================== 既存互換コンストラクタ ===================== */

    /** 旧方式との互換：ErrorCode のみを渡す（enum に定義された key を使用） */
    public BizException(ErrorCode code) {
        super(code.message());            // 一時的に key を Throwable#getMessage() に保存
        this.code = code;
        this.messageKey = code.message();
        this.args = null;
        this.detail = null;
    }

    /** 推奨：業務用の key とプレースホルダ引数を指定 */
    public BizException(ErrorCode code, String messageKey, Object... args) {
        super(messageKey);
        this.code = code;
        this.messageKey = messageKey;
        this.args = args;
        this.detail = null;
    }

    /** 原因例外（チェーン例外）を伴う場合 */
    public BizException(ErrorCode code, Throwable cause) {
        super(code.message(), cause);
        this.code = code;
        this.messageKey = code.message();
        this.args = null;
        this.detail = null;
    }

    public BizException(ErrorCode code, String messageKey, Throwable cause, Object... args) {
        super(messageKey, cause);
        this.code = code;
        this.messageKey = messageKey;
        this.args = args;
        this.detail = null;
    }

    /* ===================== detail 追加コンストラクタ ===================== */

    /** ErrorCode + detail（i18n key は ErrorCode 既定を使用） */
    public BizException(ErrorCode code, String detail) {
        super(code.message());
        this.code = code;
        this.messageKey = code.message();
        this.args = null;
        this.detail = detail;
    }

    /** ErrorCode + messageKey(+args) + detail */
    public BizException(ErrorCode code, String messageKey, String detail, Object... args) {
        super(messageKey);
        this.code = code;
        this.messageKey = messageKey;
        this.args = args;
        this.detail = detail;
    }

    /** ErrorCode + cause + detail（i18n key は ErrorCode 既定を使用） */
    public BizException(ErrorCode code, Throwable cause, String detail) {
        super(code.message(), cause);
        this.code = code;
        this.messageKey = code.message();
        this.args = null;
        this.detail = detail;
    }

    public BizException(ErrorCode code, String messageKey, Throwable cause, String detail, Object... args) {
        super(messageKey, cause);
        this.code = code;
        this.messageKey = messageKey;
        this.args = args;
        this.detail = detail;
    }

    /* ===================== 使いやすいファクトリ（任意） ===================== */

    public static BizException of(ErrorCode code) {
        return new BizException(code);
    }

    public static BizException of(ErrorCode code, String messageKey, Object... args) {
        return new BizException(code, messageKey, args);
    }

    public static BizException withDetail(ErrorCode code, String detail) {
        return new BizException(code, detail);
    }

    public static BizException withDetail(ErrorCode code, String messageKey, String detail, Object... args) {
        return new BizException(code, messageKey, detail, args);
    }

    public static BizException withCause(ErrorCode code, Throwable cause) {
        return new BizException(code, cause);
    }

    public static BizException withCause(ErrorCode code, Throwable cause, String messageKey, Object... args) {
        return new BizException(code, messageKey, cause, args);
    }

    public static BizException withCauseAndDetail(ErrorCode code, Throwable cause, String detail) {
        return new BizException(code, cause, detail);
    }

    public static BizException withCauseAndDetail(ErrorCode code, String messageKey, Throwable cause, String detail, Object... args) {
        return new BizException(code, messageKey, cause, detail, args);
    }
}
