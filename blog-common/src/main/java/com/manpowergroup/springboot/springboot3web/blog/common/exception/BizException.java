package com.manpowergroup.springboot.springboot3web.blog.common.exception;

import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import lombok.Getter;

/** 业务异常：承载 ErrorCode + i18n 的 messageKey + 可选占位参数 */
@Getter
public class BizException extends RuntimeException {

    private final ErrorCode code;
    /** i18n 文案 key，如 "error.not_found" / "article.not_found" / "field.required" */
    private final String messageKey;
    /** i18n 占位参数：对应 properties 中 {0},{1}... */
    private final Object[] args;

    /** 兼容旧用法：只传 ErrorCode（使用枚举自带的 key） */
    public BizException(ErrorCode code) {
        super(code.message());            // 暂存 key 到 Throwable#getMessage()
        this.code = code;
        this.messageKey = code.message(); // 等价于 code.messageKey()
        this.args = null;
    }

    /** 推荐：指定业务 key + 占位参数 */
    public BizException(ErrorCode code, String messageKey, Object... args) {
        super(messageKey);
        this.code = code;
        this.messageKey = messageKey;
        this.args = args;
    }

    /** 带根因（链路异常） */
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
