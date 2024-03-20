package com.sparta.spartdelivery.external.security;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 설정
        http.csrf((csrf) -> csrf.disable());

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
//                        .requestMatchers("/").permitAll() // 메인 페이지 요청 허가
                        .requestMatchers("/emailConfirm").permitAll() // 로그인 전단계인, 이메일인증 요청 접근허가
                        .requestMatchers("/emailConfirm/**").permitAll() // 로그인 전단계인, 이메일인증 요청 접근허가
                        .requestMatchers("/login").permitAll() // 로그인 페이지 접근허가
                        .requestMatchers("/login2").permitAll() // 로그인 페이지 접근허가
                        .requestMatchers("/signup").permitAll() // 로그인 전단계인, 회원가입 요청 접근허가
                        .requestMatchers("/store").permitAll()
                        .requestMatchers("/store/create-store").hasAuthority("ROLE_BOSS")
                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );

//        // 로그인 사용
//        http.formLogin((formLogin) ->
//                        formLogin
//                                // 로그인 View 제공 (GET /api/user/login-page)
//                                .loginPage("/login")
//                                // 로그인 처리 (POST /api/user/login)
//                                .loginProcessingUrl("/login2")
//                                // 로그인 처리 후 성공 시 URL//
//                                 .defaultSuccessUrl("/store/stores")
//                                // 로그인 처리 후 실패 시 URL
//                                .failureUrl("/login?error")
//                                .permitAll()
//        );



        // 필터 관리
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}