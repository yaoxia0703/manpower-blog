package com.manpowergroup.springboot.springboot3web.framework.security.jwt;

import com.manpowergroup.springboot.springboot3web.framework.security.authority.UserAuthorityProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserAuthorityProvider userAuthorityProvider;

    public JwtAuthenticationFilter(
            JwtTokenProvider jwtTokenProvider,
            UserAuthorityProvider userAuthorityProvider
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userAuthorityProvider = userAuthorityProvider;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        final String path = request.getRequestURI();
        if (path == null || path.isBlank()) {
            return false;
        }

        return path.startsWith("/api/system/auth/")
                || path.startsWith("/error/")
                || path.equals("/favicon.ico");

    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String token = resolveToken(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!jwtTokenProvider.validate(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        final Long userId = jwtTokenProvider.getUserId(token);
        if (userId == null) {
            filterChain.doFilter(request, response);
            return;
        }

        final List<String> permissionCodes = userAuthorityProvider.loadPermissionCodes(userId);

        final List<SimpleGrantedAuthority> authorities = permissionCodes.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .distinct()
                .map(SimpleGrantedAuthority::new)
                .toList();

        final var principal = new LoginPrincipal(userId);

        final UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(principal, null, authorities);

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || header.isBlank()) {
            return null;
        }

        final String prefix = "Bearer ";
        if (header.length() < prefix.length()) {
            return null;
        }
        if (!header.regionMatches(true, 0, prefix, 0, prefix.length())) {
            return null;
        }

        final String token = header.substring(prefix.length()).trim();
        return token.isBlank() ? null : token;
    }
}
