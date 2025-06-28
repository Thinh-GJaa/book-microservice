package com.book.orderservice.config;

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
        "/v3/api-docs",
        "/v3/api-docs/**",
        "/swagger-ui.html",
        "/swagger-ui/**",
        "/swagger-resources",
        "/swagger-resources/**",
        "/webjars/**"
        // Thêm các endpoint public khác nếu có
    };

    private final CustomJwtDecoder customJwtDecoder;

    public SecurityConfig(CustomJwtDecoder customJwtDecoder) {
        this.customJwtDecoder = customJwtDecoder;
    }

    // Hàm kiểm tra request có phải là public endpoint không
    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getServletPath();
        for (String endpoint : PUBLIC_ENDPOINTS) {
            if (endpoint.endsWith("/**")) {
                if (path.startsWith(endpoint.replace("/**", ""))) {
                    return true;
                }
            } else if (path.equals(endpoint)) {
                return true;
            }
        }
        return false;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        log.info("Configuring SecurityFilterChain for Order Service");
        httpSecurity.authorizeHttpRequests(request -> request
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                .anyRequest().authenticated());

        httpSecurity.addFilterBefore((servletRequest, servletResponse, filterChain) -> {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            log.info("[SECURITY][HEADER] Authorization: [{}]", req.getHeader(HttpHeaders.AUTHORIZATION));
            if (isPublicEndpoint(req)) {
                log.info("[SECURITY][PUBLIC] [{}] {} accessed URI: {}", req.getMethod(), req.getRemoteAddr(), req.getRequestURI());
            } else {
                log.info("[SECURITY][AUTHENTICATED] [{}] {} accessed URI: {}", req.getMethod(), req.getRemoteAddr(), req.getRequestURI());
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }, org.springframework.web.filter.CorsFilter.class);

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
