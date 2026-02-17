package com.manpowergroup.springboot.springboot3web.framework.security.jwt;

/**
 * JWT 認証後に SecurityContext に入れる最小 principal
 * まずは userId のみ（将来 employeeNo / role などを拡張可能）
 */
public record LoginPrincipal(Long userId) {
}
