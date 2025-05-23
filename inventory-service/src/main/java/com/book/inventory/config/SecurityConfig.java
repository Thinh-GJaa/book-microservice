package com.book.inventory.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {

    private static final String[] PUBLIC_ENDPOINTS = {


    };

    private final CustomJwtDecoder customJwtDecoder;

    public SecurityConfig(CustomJwtDecoder customJwtDecoder) {
        this.customJwtDecoder = customJwtDecoder;
    }

    // Hàm kiểm tra request có phải là public endpoint không
    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getServletPath();
        String method = request.getMethod();
        for (String endpoint : PUBLIC_ENDPOINTS) {
            // Nếu chỉ cho phép POST cho các endpoint public
            if (path.equals(endpoint) && "POST".equalsIgnoreCase(method)) {
                return true;
            }
        }
        return false;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Xác thực các endpoint công khai
        httpSecurity.authorizeHttpRequests(request -> request
                // Cho phép các endpoint công khai
                .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                // Các endpoint khác yêu cầu xác thực
                .anyRequest().authenticated());

        // Log endpoint public hoặc cần xác thực
        httpSecurity.addFilterBefore((servletRequest, servletResponse, filterChain) -> {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            log.info("header: {}", req.getHeader(HttpHeaders.AUTHORIZATION));
            if (isPublicEndpoint(req)) {
                log.info("Public endpoint accessed: {} {}", req.getMethod(), req.getRequestURI());
            } else {
                log.info("Endpoint yêu cầu xác thực: {} {}", req.getMethod(), req.getRequestURI());
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }, org.springframework.web.filter.CorsFilter.class);

        // Cấu hình OAuth2 Resource Server
        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(customJwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

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

}
