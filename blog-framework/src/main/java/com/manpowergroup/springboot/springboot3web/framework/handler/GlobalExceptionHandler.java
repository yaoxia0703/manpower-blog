package com.manpowergroup.springboot.springboot3web.framework.handler;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.Result;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.ValidationErrors;
import com.manpowergroup.springboot.springboot3web.blog.common.enums.ErrorCode;
import com.manpowergroup.springboot.springboot3web.blog.common.exception.BizException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Locale;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final MessageSource messageSource;
    private final Environment env;

    public GlobalExceptionHandler(MessageSource messageSource, Environment env) {
        this.messageSource = messageSource;
        this.env = env;
    }

    /* ====================== 共通ユーティリティ ====================== */

    /** 現在の環境が prod かどうかを判定 */
    private boolean isProd() {
        for (String p : env.getActiveProfiles()) {
            if ("prod".equalsIgnoreCase(p)) return true;
        }
        return false;
    }

    /** prod 環境では detail を非表示（null にする） */
    private String safeDetail(String detail) {
        return isProd() ? null : detail;
    }

    /** 現在の Locale でメッセージコードを翻訳する */
    private String i18n(String codeOrRaw, Object... args) {
        if (codeOrRaw == null || codeOrRaw.isBlank()) return "";
        Locale locale = LocaleContextHolder.getLocale();
        try {
            return messageSource.getMessage(codeOrRaw, args, locale);
        } catch (NoSuchMessageException ignore) {
            return codeOrRaw;
        }
    }

    /** 共通ログ出力：traceId + message + detail */
    private void logError(String message, String detail, Throwable e) {
        String traceId = MDC.get("traceId");
        if (e != null) {
            log.error("[traceId={}] {} | {}", traceId, message, detail, e);
        } else {
            log.error("[traceId={}] {} | {}", traceId, message, detail);
        }
    }

    /* ====================== 業務例外 ====================== */
    @ExceptionHandler(BizException.class)
    public Result<Object> handleBiz(BizException e) {
        String key = (e.getMessage() == null || e.getMessage().isBlank())
                ? ErrorCode.BIZ_ERROR.message()
                : e.getMessage();
        int code = (e.getCode() != null) ? e.getCode().code() : ErrorCode.BIZ_ERROR.code();
        String msg = i18n(key);
        String detail = i18n(key);

        logError(msg, detail, e);
        return Result.error(code, msg).withDetail(safeDetail(detail));
    }

    /* ====================== バリデーション例外 ====================== */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<ValidationErrors> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        var items = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> ValidationErrors.ErrorItem.of(
                        fe.getField(),
                        fe.getDefaultMessage(),
                        fe.getField()
                ))
                .toList();

        String msg = i18n(ErrorCode.VALIDATION_ERROR.message());
        String detail = "入力検証に失敗しました（" + items.size() + "件）";
        logError(msg, detail, e);

        return Result.of(ErrorCode.VALIDATION_ERROR.code(), msg, ValidationErrors.of(items))
                .withDetail(safeDetail(detail));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<ValidationErrors> handleConstraintViolation(ConstraintViolationException e) {
        var items = e.getConstraintViolations().stream()
                .map(v -> {
                    String path = v.getPropertyPath().toString();
                    String field = path.contains(".") ? path.substring(path.lastIndexOf('.') + 1) : path;
                    return ValidationErrors.ErrorItem.of(field, v.getMessage(), field);
                })
                .toList();

        String msg = i18n(ErrorCode.VALIDATION_ERROR.message());
        String detail = "ConstraintViolation " + items.size() + "件";
        logError(msg, detail, e);

        return Result.of(ErrorCode.VALIDATION_ERROR.code(), msg, ValidationErrors.of(items))
                .withDetail(safeDetail(detail));
    }

    /* ====================== よくある Web 例外 ====================== */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<ValidationErrors> handleMissingParam(MissingServletRequestParameterException e) {
        String msg = i18n("error.missing_param", e.getParameterName());
        String detail = "Missing parameter: " + e.getParameterName();
        logError(msg, detail, e);

        var item = ValidationErrors.ErrorItem.of(e.getParameterName(), "error.missing_param", e.getParameterName());
        return Result.of(ErrorCode.BAD_REQUEST.code(), msg, ValidationErrors.of(java.util.List.of(item)))
                .withDetail(safeDetail(detail));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Object> handleNotReadable(HttpMessageNotReadableException e) {
        String msg = i18n(ErrorCode.BAD_REQUEST.message());
        String detail = e.getMessage();
        logError(msg, detail, e);
        return Result.error(ErrorCode.BAD_REQUEST.code(), msg).withDetail(safeDetail(detail));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Object> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
        String msg = i18n(ErrorCode.METHOD_NOT_ALLOWED.message());
        String detail = e.getMessage();
        logError(msg, detail, e);
        return Result.error(ErrorCode.METHOD_NOT_ALLOWED.code(), msg).withDetail(safeDetail(detail));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Result<Object> handleMediaType(HttpMediaTypeNotSupportedException e) {
        String msg = i18n(ErrorCode.UNSUPPORTED_MEDIA_TYPE.message());
        String detail = e.getMessage();
        logError(msg, detail, e);
        return Result.error(ErrorCode.UNSUPPORTED_MEDIA_TYPE.code(), msg).withDetail(safeDetail(detail));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result<Object> handleAccessDenied(AccessDeniedException e) {
        String msg = i18n(ErrorCode.FORBIDDEN.message());
        String detail = e.getMessage();
        logError(msg, detail, e);
        return Result.error(ErrorCode.FORBIDDEN.code(), msg).withDetail(safeDetail(detail));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Result<Object> handleNoResourceFound(NoResourceFoundException e) {
        String msg = i18n(ErrorCode.NOT_FOUND.message());
        String detail = e.getMessage();
        logError(msg, detail, e);
        return Result.error(ErrorCode.NOT_FOUND.code(), msg).withDetail(safeDetail(detail));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<Object> handleMaxUpload(MaxUploadSizeExceededException e) {
        String msg = i18n("error.upload.too_large");
        String detail = e.getMessage();
        logError(msg, detail, e);
        return Result.error(413, msg).withDetail(safeDetail(detail));
    }

    /* ====================== その他例外（500） ====================== */
    @ExceptionHandler(Exception.class)
    public Result<Object> handleOther(Exception e) {
        String msg = i18n(ErrorCode.SERVER_ERROR.message());
        String detail = e.getMessage();
        logError(msg, detail, e);
        return Result.error(ErrorCode.SERVER_ERROR.code(), msg).withDetail(safeDetail(detail));
    }
}
