package com.manpowergroup.springboot.springboot3web.blog.common.exception;


import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;

/**
 * 业务异常（默认 460）
 */
public class BizException extends RuntimeException {
    private final int code;

    public BizException(String message) {
        super(message);
        this.code = ErrorCode.BIZ_ERROR.code();
    }

    public BizException(ErrorCode code, String message) {
        super(message);
        this.code = code.code();
    }

    public BizException(ErrorCode code) {
        super(code.message());
        this.code = code.code();
    }

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
