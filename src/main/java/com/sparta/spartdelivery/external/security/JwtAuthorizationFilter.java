package com.sparta.spartdelivery.external.security;

import com.sparta.spartdelivery.enums.UserRoleEnum;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String accessTokenValue = jwtUtil.getTokenFromRequest(req, "accessToken");
        String refreshTokenValue = jwtUtil.getTokenFromRequest(req, "refreshToken");

        if (StringUtils.hasText(accessTokenValue)) {
            accessTokenValue = jwtUtil.substringToken(accessTokenValue); // "Bearer " 제거

            if (!jwtUtil.validateToken(accessTokenValue)) {
                // Access Token이 만료되었을 경우 Refresh Token 검사
                if (StringUtils.hasText(refreshTokenValue)) {
                    refreshTokenValue = jwtUtil.substringToken(refreshTokenValue); // "Bearer " 제거

                    if (jwtUtil.validateToken(refreshTokenValue)) {
                        Claims refreshTokenInfo = jwtUtil.getUserInfoFromToken(refreshTokenValue);
                        String username = refreshTokenInfo.getSubject();
                        UserRoleEnum role = UserRoleEnum.valueOf(refreshTokenInfo.get(JwtUtil.AUTHORIZATION_KEY, String.class));

                        setAuthentication(username);

                        // 새로운 AccessToken 생성
                        String newAccessToken = jwtUtil.createAccessToken(username, role);

                        // 생성된 AccessToken을 응답에 추가
                        res.addHeader("accessToken", newAccessToken);
                        jwtUtil.addJwtToCookie(newAccessToken, res, "accessToken");
                    } else {
                        // Refresh Token도 만료된 경우
                        removeTokens(res);
                        res.sendRedirect(req.getContextPath() + "/login"); //login페이지로 리다이렉트
                    }
                } else {
                    // Refresh Token이 없는 경우
                    removeTokens(res);
                }
            } else {
                Claims accessTokenInfo = jwtUtil.getUserInfoFromToken(accessTokenValue);
                String username = accessTokenInfo.getSubject();

                setAuthentication(username);
            }
        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    // Token 제거
    private void removeTokens(HttpServletResponse res) {
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0);
        res.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        res.addCookie(refreshTokenCookie);
    }
}