package com.manpowergroup.springboot.springboot3web.blog.common.enums;

public enum UserErrorCode implements BaseErrorCode {

    ACCOUNT_NOT_FOUND(401, "error.account_not_found"),
    PASSWORD_INCORRECT(401, "error.password_incorrect"),
    INVALID_ACCOUNT_DATA(400, "error.invalid_account_data"),
    ACCOUNT_DISABLED(403, "error.account_disabled");


    private final int code;
    private final String messageKey;

    UserErrorCode(int code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    public int code() {
        return code;
    }

    public String messageKey() {
        return messageKey;
    }
}