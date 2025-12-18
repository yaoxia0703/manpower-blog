package com.manpowergroup.springboot.springboot3web.framework.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 設定クラス
 * infra.redis.enabled=true の場合のみ有効化される
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RedisProperties.class)
@ConditionalOnProperty(prefix = "infra.redis", name = "enabled", havingValue = "true")
public class RedisConfig {

    /**
     * Redis 接続ファクトリの設定（スタンドアロン構成）
     */
    @Bean
    public LettuceConnectionFactory redisConnectionFactory(RedisProperties props) {
        RedisStandaloneConfiguration cfg =
                new RedisStandaloneConfiguration(props.getHost(), props.getPort());
        cfg.setDatabase(props.getDatabase());
        if (props.getPassword() != null && !props.getPassword().isBlank()) {
            cfg.setPassword(props.getPassword());
        }
        return new LettuceConnectionFactory(cfg);
    }

    /**
     * Object 用 RedisTemplate 設定
     * キーは String、値は JSON 形式でシリアライズする
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory cf) {
        RedisTemplate<String, Object> t = new RedisTemplate<>();
        t.setConnectionFactory(cf);
        t.setKeySerializer(new StringRedisSerializer());
        t.setHashKeySerializer(new StringRedisSerializer());
        GenericJackson2JsonRedisSerializer json = new GenericJackson2JsonRedisSerializer();
        t.setValueSerializer(json);
        t.setHashValueSerializer(json);
        t.afterPropertiesSet();
        return t;
    }

    /**
     * String 専用 RedisTemplate 設定
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory cf) {
        return new StringRedisTemplate(cf);
    }
}
