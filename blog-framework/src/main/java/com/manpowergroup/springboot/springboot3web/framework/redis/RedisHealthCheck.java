package com.manpowergroup.springboot.springboot3web.framework.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedisHealthCheck {

    private final ObjectProvider<StringRedisTemplate> provider;

    // 这个 Bean 总是注册，用属性判断是否执行
    @Bean
    ApplicationRunner redisPingOnStartup(
            @org.springframework.beans.factory.annotation.Value("${infra.redis.enabled:false}") boolean enabled) {
        return args -> {
            if (!enabled) {
                log.info("Redis 已禁用，跳过启动自检。");
                return;
            }
            var srt = provider.getIfAvailable();
            if (srt == null) {
                log.warn("Redis 已启用但未装配，跳过启动自检。");
                return;
            }
            try {
                var pong = srt.getRequiredConnectionFactory().getConnection().ping();
                log.info("Redis 自检成功: {}", pong);
            } catch (Exception e) {
                log.error("Redis 自检失败: {}", e.getMessage(), e);
            }
        };
    }
}

