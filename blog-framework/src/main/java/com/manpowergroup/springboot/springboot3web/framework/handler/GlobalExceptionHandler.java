package com.manpowergroup.springboot.springboot3web.framework.handler;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.Result;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.ValidationErrors;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * 用当前 Locale 翻译消息码；若找不到对应 key，则原文回退
     */
    private String i18n(String codeOrRaw, Object... args) {
        if (codeOrRaw == null || codeOrRaw.isBlank()) return "";
        Locale locale = LocaleContextHolder.getLocale();
        try {
            return messageSource.getMessage(codeOrRaw, args, locale);
        } catch (NoSuchMessageException ignore) {
            // 回退原文，兼容旧逻辑（你原来的 e.getMessage() 等）
            return codeOrRaw;
        }
    }

    /* ====================== 业务异常 ====================== */
    @ExceptionHandler(BizException.class)
    public Result<Object> handleBiz(BizException e) {
        // 约定：e.getMessage() 是消息码 key（例如 "slug.duplicated" / "article.not_found"）
        String key = (e.getMessage() == null || e.getMessage().isBlank())
                ? ErrorCode.BIZ_ERROR.message()
                : e.getMessage();

        int code = (e.getCode() != null) ? e.getCode().code() : ErrorCode.BIZ_ERROR.code();

        // 顶层 message 返回 key；data 暂不下沉 args（后续需要再加）
        return Result.error(code, key);
    }



    /* ====================== 校验异常 ====================== */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        var items = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> ValidationErrors.ErrorItem.of(
                        fe.getField(),
                        fe.getDefaultMessage(),   // 直接返回 key
                        fe.getField()             // 占位符参数
                ))
                .toList();

        return Result.of(ErrorCode.VALIDATION_ERROR.code(),
                ErrorCode.VALIDATION_ERROR.message(),
                ValidationErrors.of(items));
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Object> handleConstraintViolation(ConstraintViolationException e) {
        var items = e.getConstraintViolations().stream()
                .map(v -> {
                    String path = v.getPropertyPath().toString();
                    String field = path.contains(".") ? path.substring(path.lastIndexOf('.') + 1) : path;
                    return ValidationErrors.ErrorItem.of(
                            field,
                            v.getMessage(),   // 这里也应该是 key
                            field             // 占位符参数
                    );
                })
                .toList();

        return Result.of(ErrorCode.VALIDATION_ERROR.code(),
                ErrorCode.VALIDATION_ERROR.message(),
                ValidationErrors.of(items));
    }


    private String formatFieldError(FieldError fe) {
        String field = fe.getField();
        // 注解里的 message 通常可写“消息码”，这里按 i18n 翻译；没有就回退原文
        String translated = i18n(Objects.toString(fe.getDefaultMessage(), "invalid"));
        return field + ": " + translated;
    }

    private String formatConstraintViolation(ConstraintViolation<?> v) {
        // v.getMessage() 同样当作消息码处理
        String translated = i18n(Objects.toString(v.getMessage(), "invalid"));
        return v.getPropertyPath() + ": " + translated;
    }

    /* ====================== 常见 Web 异常 ====================== */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Object> handleMissingParam(MissingServletRequestParameterException e) {
        var item = ValidationErrors.ErrorItem.of(
                e.getParameterName(),
                "error.missing_param",
                e.getParameterName()
        );

        return Result.of(ErrorCode.BAD_REQUEST.code(),
                "error.missing_param",
                ValidationErrors.of(java.util.List.of(item)));
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Object> handleNotReadable(HttpMessageNotReadableException e) {
        return Result.error(ErrorCode.BAD_REQUEST.code(), ErrorCode.BAD_REQUEST.message());
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Object> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
        return Result.error(ErrorCode.METHOD_NOT_ALLOWED.code(), ErrorCode.METHOD_NOT_ALLOWED.message());
    }


    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Result<Object> handleMediaType(HttpMediaTypeNotSupportedException e) {
        return Result.error(ErrorCode.UNSUPPORTED_MEDIA_TYPE.code(), ErrorCode.UNSUPPORTED_MEDIA_TYPE.message());
    }


    @ExceptionHandler(AccessDeniedException.class)
    public Result<Object> handleAccessDenied(AccessDeniedException e) {
        return Result.error(ErrorCode.FORBIDDEN.code(), ErrorCode.FORBIDDEN.message());
    }


    // 可选：开启 404 友好提示
    @ExceptionHandler(NoResourceFoundException.class)
    public Result<Object> handleNoResourceFound(NoResourceFoundException e) {
        return Result.error(ErrorCode.NOT_FOUND.code(), ErrorCode.NOT_FOUND.message());
    }


    /* ====================== 兜底异常（500） ====================== */
    @ExceptionHandler(Exception.class)
    public Result<Object> handleOther(Exception e) {
        return Result.error(ErrorCode.SERVER_ERROR.code(), ErrorCode.SERVER_ERROR.message());
    }

}
