package com.book.bookservice.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {

    private static final String[] PUBLIC_ENDPOINTS = {
            "/ids",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/webjars/**"
    };

    private final CustomJwtDecoder customJwtDecoder;

    public SecurityConfig(CustomJwtDecoder customJwtDecoder) {
        this.customJwtDecoder = customJwtDecoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> request
                // Allow all HTTP methods for public endpoints
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                // All other endpoints require authentication
                .anyRequest().authenticated());

        // Log public or authenticated endpoint access
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
        grantedAuthoritiesConverter.setAuthorityPrefix(""); // Do not add "ROLE_" prefix again
        grantedAuthoritiesConverter.setAuthoritiesClaimName("role"); // Use the correct claim name

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    private static boolean publicEndpoint(HttpServletRequest request) {
        String path = request.getServletPath();
        return Arrays.stream(PUBLIC_ENDPOINTS)
                .anyMatch(pattern -> pattern.endsWith("/**") ? path.startsWith(pattern.replace("/**", ""))
                        : path.equals(pattern));
    }

}
