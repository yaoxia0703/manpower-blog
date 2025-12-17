package com.manpowergroup.springboot.springboot3web.blog.common.exception;

import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import lombok.Getter;

/** 業務例外：ErrorCode + i18n の messageKey + 任意のプレースホルダ引数を保持 */
@Getter
public class BizException extends RuntimeException {

    private final ErrorCode code;
    /** i18n 文言キー（例: "error.not_found" / "article.not_found" / "field.required"） */
    private final String messageKey;
    /** i18n プレースホルダ引数：properties 内の {0},{1}... に対応 */
    private final Object[] args;

    /** 旧方式との互換：ErrorCode のみを渡す（enum に定義された key を使用） */
    public BizException(ErrorCode code) {
        super(code.message());            // 一時的に key を Throwable#getMessage() に保存
        this.code = code;
        this.messageKey = code.message(); // code.messageKey() と同等
        this.args = null;
    }

    /** 推奨：業務用の key とプレースホルダ引数を指定 */
    public BizException(ErrorCode code, String messageKey, Object... args) {
        super(messageKey);
        this.code = code;
        this.messageKey = messageKey;
        this.args = args;
    }

    /** 原因例外（チェーン例外）を伴う場合 */
    public BizException(ErrorCode code, Throwable cause) {
        super(code.message(), cause);
        this.code = code;
        this.messageKey = code.message();
        this.args = null;
    }

    public BizException(ErrorCode code, String messageKey, Throwable cause, Object... args) {
        super(messageKey, cause);
        this.code = code;
        this.messageKey = messageKey;
        this.args = args;
    }
}
