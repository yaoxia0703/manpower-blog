package com.manpowergroup.springboot.springboot3web.blog.common.enums;

/** 统一错误码（可逐步扩展业务码 1000+） */
public enum ErrorCode {
    SUCCESS(200, "success.ok"),
    BAD_REQUEST(400, "error.bad_request"),
    UNAUTHORIZED(401, "error.unauthorized"),
    FORBIDDEN(403, "error.forbidden"),
    NOT_FOUND(404, "error.not_found"),
    METHOD_NOT_ALLOWED(405, "error.method_not_allowed"),
    UNSUPPORTED_MEDIA_TYPE(415, "error.unsupported_media_type"),
    TOO_MANY_REQUESTS(429, "error.too_many_requests"),
    VALIDATION_ERROR(422, "error.validation"),
    /** 推荐用 409（Conflict）表示业务冲突，也可保留 460 */
    BIZ_ERROR(409, "error.business"),
    SERVER_ERROR(500, "error.server"),
    SERVICE_UNAVAILABLE(503, "error.unavailable");

    private final int code;
    /** 这里不再是默认文案，而是 i18n 的 messageKey */
    private final String messageKey;

    ErrorCode(int code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    public int code() {
        return code;
    }

    /** 兼容旧调用：暂时让 message() 返回 key，后续由 Handler 做解析 */
    public String message() {
        return messageKey;
    }

    public String messageKey() {
        return messageKey;
    }
}
