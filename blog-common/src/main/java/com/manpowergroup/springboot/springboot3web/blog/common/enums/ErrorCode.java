package com.manpowergroup.springboot.springboot3web.blog.common.enums;

/**
 * 統一エラーコード（業務コードは 1000+ から拡張可能）
 */
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

    // ===== 併走/排他制御（競合）=====
    // 同時実行などにより処理が衝突した場合（例：ロック取得失敗）
    CONFLICT(409, "error.conflict"),

    BIZ_ERROR(409, "error.business"),
    SERVER_ERROR(500, "error.server"),
    SERVICE_UNAVAILABLE(503, "error.unavailable"),

    // ========================
    // ファイル関連エラー
    // ========================
    FILE_NOT_FOUND(404, "error.file.not_found"),
    FILE_UNREADABLE(403, "error.file.unreadable"),
    FILE_EMPTY(422, "error.file.empty"),
    FILE_UNSUPPORTED(415, "error.file.unsupported"),
    FILE_IO_ERROR(500, "error.file.io"),

    // ========================
    // Excel / データ構造関連
    // ========================
    SHEET_MISSING(422, "error.sheet.missing"),
    HEADER_MISSING(422, "error.header.missing"),
    HEADER_INVALID(422, "error.header.invalid"),
    DATA_FORMAT_ERROR(422, "error.data.format"),
    DATA_VALIDATION_FAILED(422, "error.data.validation"),

    // ========================
    // インポート処理関連
    // ========================
    IMPORT_FAILED(500, "error.import.failed"),
    IMPORT_ABORTED(500, "error.import.aborted"),
    IMPORT_PARTIAL_SUCCESS(206, "success.import.partial");

    private final int code;
    private final String messageKey;

    ErrorCode(int code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    public int code() {
        return code;
    }

    public String message() {
        return messageKey;
    }

    public String messageKey() {
        return messageKey;
    }
}
