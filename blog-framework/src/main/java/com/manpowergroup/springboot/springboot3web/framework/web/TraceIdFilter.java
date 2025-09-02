package com.manpowergroup.springboot.springboot3web.framework.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // 最先执行，保证后续日志都有 traceId
public class TraceIdFilter extends OncePerRequestFilter {

    public static final String TRACE_ID = "traceId";
    public static final String TRACE_HEADER = "X-Trace-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1) 优先使用上游传来的 X-Trace-Id；没有就生成一个
        String traceId = request.getHeader(TRACE_HEADER);
        if (traceId == null || traceId.isBlank()) {
            // 去掉连字符的 UUID，更短好看
            traceId = UUID.randomUUID().toString().replace("-", "");
        }

        // 2) 放到 MDC，供日志 Pattern 使用
        MDC.put(TRACE_ID, traceId);
        try {
            // 3) 回写到响应头，便于前端/网关拿到
            response.setHeader(TRACE_HEADER, traceId);
            filterChain.doFilter(request, response);
        } finally {
            // 4) 清理，避免线程复用造成串日志
            MDC.remove(TRACE_ID);
        }
    }
}
