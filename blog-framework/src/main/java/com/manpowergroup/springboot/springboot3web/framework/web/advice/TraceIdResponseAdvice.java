package com.manpowergroup.springboot.springboot3web.framework.web.advice;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.Result;
import com.manpowergroup.springboot.springboot3web.framework.web.TraceIdFilter;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Controller のレスポンスに traceId を付与するアドバイス
 */
@RestControllerAdvice
public class TraceIdResponseAdvice implements ResponseBodyAdvice<Object> {

    /**
     * traceId 付与の有効／無効設定
     */
    @Value("${app.trace-id.enabled:true}")
    private boolean traceIdEnabled;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        // すべてのレスポンスを対象とする
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        if (!traceIdEnabled) {
            return body;
        }

        if (body instanceof Result<?> result) {
            String traceId = MDC.get(TraceIdFilter.TRACE_ID);
            if (traceId != null) {
                result.withTraceId(traceId);
            }
            return result;
        }

        return body;
    }
}
