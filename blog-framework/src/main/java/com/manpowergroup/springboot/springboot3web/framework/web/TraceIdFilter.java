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
@Order(Ordered.HIGHEST_PRECEDENCE) // 最優先で実行し、後続ログがすべて traceId を持つようにする
public class TraceIdFilter extends OncePerRequestFilter {

    public static final String TRACE_ID = "traceId";
    public static final String TRACE_HEADER = "X-Trace-Id";
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(TraceIdFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1) 上流から渡された X-Trace-Id を優先使用。存在しなければ生成する
        String traceId = request.getHeader(TRACE_HEADER);
        if (traceId == null || traceId.isBlank()) {
            // ハイフンを除いた UUID。短く見やすい
            traceId = UUID.randomUUID().toString().replace("-", "");
        }

        // 2) MDC に設定し、ログのパターンで利用できるようにする
        MDC.put(TRACE_ID, traceId);
        try {
            // 3) レスポンスヘッダーに書き戻し、フロントエンド/ゲートウェイが取得できるようにする
            response.setHeader(TRACE_HEADER, traceId);
            filterChain.doFilter(request, response);
        } finally {
            // 4) スレッドの再利用によるログ混在を避けるためクリア
            MDC.remove(TRACE_ID);
        }
    }
}
