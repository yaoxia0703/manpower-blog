package com.manpowergroup.springboot.springboot3web.blog.common.dto;

import java.util.List;

public record ValidationErrors(List<ErrorItem> errors) {
    public static ValidationErrors of(List<ErrorItem> list) { return new ValidationErrors(list); }

    public record ErrorItem(String field, String key, Object[] args) {
        public static ErrorItem of(String field, String key, Object... args) {
            return new ErrorItem(field, key, args);
        }
    }
}
