package com.manpowergroup.springboot.springboot3web.blog.common.enums;

/** 统一错误码（可逐步扩展业务码 1000+） */
public enum ErrorCode {
    SUCCESS(200, "success"),
    BAD_REQUEST(400, "Bad request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not found"),
    METHOD_NOT_ALLOWED(405, "Method not allowed"),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported media type"),
    TOO_MANY_REQUESTS(429, "Too many requests"),
    VALIDATION_ERROR(422, "Validation error"),
    BIZ_ERROR(460, "Business error"),
    SERVER_ERROR(500, "Internal server error"),
    SERVICE_UNAVAILABLE(503, "Service unavailable");

    private final int code;
    private final String defaultMessage;

    ErrorCode(int code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
    public int code() { return code; }
    public String message() { return defaultMessage; }
}
