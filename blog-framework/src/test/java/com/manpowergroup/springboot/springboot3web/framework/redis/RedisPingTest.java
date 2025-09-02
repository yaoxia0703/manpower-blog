package com.manpowergroup.springboot.springboot3web.framework.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = RedisConfig.class)
@TestPropertySource(properties = {
        "infra.redis.enabled=true",
        "spring.redis.host=127.0.0.1",
        "spring.redis.port=6379",
        "spring.redis.database=0"
})
class RedisPingTest {

    @Autowired
    private ObjectProvider<StringRedisTemplate> provider; // ← 关键改动：字段注入

    @Test
    void ping_and_set_get() {
        var srt = provider.getIfAvailable();
        assumeTrue(srt != null, "RedisTemplate 不可用，测试跳过。");

        var conn = srt.getRequiredConnectionFactory().getConnection();
        assertEquals("PONG", conn.ping());

        srt.opsForValue().set("test:ping", "ok", Duration.ofSeconds(60));
        assertEquals("ok", srt.opsForValue().get("test:ping"));
    }
}
