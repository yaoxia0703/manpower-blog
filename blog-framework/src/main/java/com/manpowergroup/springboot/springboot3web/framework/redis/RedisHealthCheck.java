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

    /**
     * Redis 起動時のヘルスチェック
     * Bean 自体は常に登録し、プロパティにより実行可否を制御する
     */
    @Bean
    ApplicationRunner redisPingOnStartup(
            @org.springframework.beans.factory.annotation.Value("${infra.redis.enabled:false}") boolean enabled) {

        return args -> {
            if (!enabled) {
                log.info("Redis は無効化されています。起動時ヘルスチェックをスキップします。");
                return;
            }

            var srt = provider.getIfAvailable();
            if (srt == null) {
                log.warn("Redis は有効ですが、StringRedisTemplate が未設定のためヘルスチェックをスキップします。");
                return;
            }

            try {
                var pong = srt.getRequiredConnectionFactory()
                        .getConnection()
                        .ping();
                log.info("Redis 起動時ヘルスチェック成功: {}", pong);
            } catch (Exception e) {
                log.error("Redis 起動時ヘルスチェック失敗: {}", e.getMessage(), e);
            }
        };
    }
}
