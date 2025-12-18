package com.manpowergroup.springboot.springboot3web.framework.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * セキュリティ設定クラス
 */
@Configuration
public class SecurityConfig {

    /**
     * SecurityFilterChain 設定
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 対策を無効化
                .csrf(csrf -> csrf.disable())
                // すべてのリクエストを許可
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                // Basic 認証を有効化（ただし全リクエスト許可）
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
