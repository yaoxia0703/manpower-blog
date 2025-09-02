package com.manpowergroup.springboot.springboot3web.framework.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 关掉 csrf
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 全部接口都放行
                )
                .httpBasic(Customizer.withDefaults()); // 保留 Basic Auth，但全放行了
        return http.build();
    }
}
