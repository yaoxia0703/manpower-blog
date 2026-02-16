package com.manpowergroup.springboot.springboot3web.framework.security.jwt;

import com.manpowergroup.springboot.springboot3web.blog.common.dto.LoginUser;
import com.manpowergroup.springboot.springboot3web.blog.common.util.StringUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final String issuer;
    private final long expireSeconds;

    public JwtTokenProvider(
            @Value("${security.jwt.secret}") String base64Secret,
            @Value("${security.jwt.issuer:springboot3web}") String issuer,
            @Value("${security.jwt.expire-seconds:7200}") long expireSeconds
    ) {
        if (base64Secret == null || base64Secret.isBlank()) {
            throw new IllegalArgumentException("security.jwt.secret is blank");
        }
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
        this.issuer = issuer;
        this.expireSeconds = expireSeconds;
    }

    /**
     * 登录成功后生成 JWT
     */
    public String generateToken(LoginUser user) {
        Objects.requireNonNull(user, "user is null");
        Objects.requireNonNull(user.getUserId(), "userId is null");

        Instant now = Instant.now();
        Instant exp = now.plusSeconds(Math.max(expireSeconds, 60));

        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(String.valueOf(user.getUserId()))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .claim("roles", String.join(",", safeList(user.getRoleNames())))
                .claim("nickName", StringUtils.nullToEmpty(user.getNickName()))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 校验 token 是否有效（签名/issuer/过期/格式）
     */
    public boolean validate(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 解析 Claims（核心方法）
     */
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .requireIssuer(issuer)
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从 token 获取 userId（subject）
     */
    public Long getUserId(String token) {
        String sub = parseClaims(token).getSubject();
        if (!StringUtils.hasText(sub)) {
            throw new IllegalArgumentException("JWT subject is blank");
        }
        return Long.valueOf(sub);
    }

    /**
     * 从 token 获取 roles（逗号分隔字符串）
     */
    public String getRoles(String token) {
        Object roles = parseClaims(token).get("roles");
        return roles == null ? "" : String.valueOf(roles);
    }

    /**
     * 从 token 获取 username
     */
    public String getUsername(String token) {
        Object username = parseClaims(token).get("username");
        return username == null ? "" : String.valueOf(username);
    }

    private List<String> safeList(List<String> list) {
        return list == null ? List.of() : list;
    }
}
