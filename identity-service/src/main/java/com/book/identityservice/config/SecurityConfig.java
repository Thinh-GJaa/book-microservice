package com.book.identityservice.config;

import com.book.identityservice.dto.ErrorResponse;
import com.book.identityservice.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class SecurityConfig {

    static final String[] PUBLIC_ENDPOINTS = {
            "/auth/token",
            "/auth/introspect",
            "/auth/logout",
            "/auth/refresh",
            "/auth/users",
            "/auth/admin",
            "/auth/forgot-password",
            "/auth/verify-reset-password-link",
            "/auth/reset-password",

            // Swagger/OpenAPI public endpoints
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/webjars/**"
    };

    final CustomJwtDecoder customJwtDecoder;

    public SecurityConfig(CustomJwtDecoder customJwtDecoder) {
        this.customJwtDecoder = customJwtDecoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> request
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                .anyRequest().authenticated());

        httpSecurity.addFilterBefore((servletRequest, servletResponse, filterChain) -> {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            log.info("[SECURITY][HEADER] Authorization: [{}]", req.getHeader(HttpHeaders.AUTHORIZATION));
            if (publicEndpoint(req)) {
                log.info("[SECURITY][PUBLIC] [{}] {} accessed URI: {}", req.getMethod(), req.getRemoteAddr(),
                        req.getRequestURI());
            } else {
                log.info("[SECURITY][AUTHENTICATED] [{}] {} accessed URI: {}", req.getMethod(), req.getRemoteAddr(),
                        req.getRequestURI());
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }, org.springframework.web.filter.CorsFilter.class);

        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(customJwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix(""); // Không thêm "ROLE_" lần nữa
        grantedAuthoritiesConverter.setAuthoritiesClaimName("role"); // Đúng tên claim bạn dùng

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        // Mã hóa mật khẩu với độ mạnh 10
        return new BCryptPasswordEncoder(10);
    }

    private static boolean publicEndpoint(HttpServletRequest request) {
        String path = request.getServletPath();
        return Arrays.stream(PUBLIC_ENDPOINTS)
                .anyMatch(pattern -> pattern.endsWith("/**") ? path.startsWith(pattern.replace("/**", ""))
                        : path.equals(pattern));
    }
}
