package com.kch.api_project.global;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /*swagger 코드는 어차피 build.gradle에 추가안했으면 있어도 작동안하니 그냥 놔둬도 작동양호*/
    /* ==================== Swagger 허용 경로 (문서/리소스) ==================== */
    private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };
    /* ====================================================================== */

    private final TokenProvider tokenProvider;
    private final CustomUserDetailService customUserDetailService;

    public SecurityConfig(TokenProvider tokenProvider, CustomUserDetailService customUserDetailService) {
        this.tokenProvider = tokenProvider;
        this.customUserDetailService = customUserDetailService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenProvider, customUserDetailService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean // 시큐리티 필터 체인
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화 (JWT 사용 시 일반적으로 비활성화)
                .csrf(AbstractHttpConfigurer::disable)

                // 세션을 사용하지 않는 Stateless 전략
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 인가(접근 제어) 설정
                .authorizeHttpRequests(auth -> auth
                        /* ==================== Swagger 구간 시작 ==================== */
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        /* ==================== Swagger 구간 끝 ====================== */

                        // 인증/회원가입 등 공개 API 경로
                        .requestMatchers("/api/auth/**").permitAll()

                        // 이외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
