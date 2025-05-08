package com.book.identityservice.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
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
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] PUBLIC_ENDPOINTS = {
            "/auth/token",
            "/auth/introspect",
            "/auth/logout",
            "/auth/refresh",
            "/auth/users"
    };

    private final CustomJwtDecoder customJwtDecoder;

    public SecurityConfig(CustomJwtDecoder customJwtDecoder) {
        this.customJwtDecoder = customJwtDecoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Xác thực các endpoint công khai
        httpSecurity.authorizeHttpRequests(request -> request
                // Cho phép các endpoint công khai
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                // Các endpoint khác yêu cầu xác thực
                .anyRequest().authenticated());

        // Cấu hình OAuth2 Resource Server
        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwtConfigurer -> jwtConfigurer
                        // Sử dụng customJwtDecoder để giải mã JWT
                        .decoder(customJwtDecoder)
                        // Cấu hình converter để chuyển đổi JWT thành đối tượng Authentication
                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                // Sử dụng entry point tập trung để trả về lỗi theo ErrorCode
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                // Xử lý khi bị từ chối truy cập
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    // Trả về lỗi theo ErrorCode.ACCESS_DENIED
                    var errorCode = com.book.identityservice.exception.ErrorCode.ACCESS_DENIED;
                    response.setStatus(errorCode.getStatus().value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    com.book.identityservice.dto.ErrorResponse errorResponse = com.book.identityservice.dto.ErrorResponse
                            .of(
                                    String.valueOf(errorCode.getCode()),
                                    errorCode.getStatus(),
                                    errorCode.getMessageTemplate(),
                                    null);
                    new com.fasterxml.jackson.databind.ObjectMapper()
                            .writeValue(response.getWriter(), errorResponse);
                    response.flushBuffer();
                }));

        // Tắt CSRF vì không cần thiết cho REST API
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // Không thêm tiền tố vào quyền (authority)
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        // Sử dụng converter để lấy danh sách quyền từ JWT
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        // Mã hóa mật khẩu với độ mạnh 10
        return new BCryptPasswordEncoder(10);
    }
}
