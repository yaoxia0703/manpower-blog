package com.manpowergroup.springboot.springboot3web.blog.common.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manpowergroup.springboot.springboot3web.blog.common.config.PageProperties;
import com.manpowergroup.springboot.springboot3web.blog.common.dto.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PageUtil {

    private final PageProperties pageProperties;

    /**
     * 型推論補助用（呼び出し側で var を使っても Page<T> を安定させる）
     */
    @SuppressWarnings("unused")
    public <T> Page<T> toPage(PageRequest request, Class<T> clazz) {
        return toPage(request);
    }

    /**
     * MyBatis-Plus の Page へ変換（安全なページング値に補正）
     */
    public <T> Page<T> toPage(PageRequest request) {
        final long safeNum = normalizePageNum(request == null ? null : request.getPageNum());
        final long safeSize = normalizePageSize(request == null ? null : request.getPageSize());
        return new Page<>(safeNum, safeSize);
    }

    private long normalizePageNum(Long value) {
        if (value == null || value <= 0) {
            return pageProperties.getDefaultPageNum();
        }
        return value;
    }

    private long normalizePageSize(Long value) {
        if (value == null || value <= 0) {
            return pageProperties.getDefaultPageSize();
        }
        return Math.min(value, pageProperties.getMaxPageSize());
    }
}
