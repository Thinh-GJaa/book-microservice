package com.book.apigateway.config;

import com.book.apigateway.dto.ApiResponse;
import com.book.apigateway.service.IdentityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class AuthenticationFilter implements GlobalFilter, Ordered {

    IdentityService identityService;
    ObjectMapper objectMapper;

    @Value("${app.api-prefix}")
    @NonFinal
    String apiPrefix;

    @NonFinal
    String[] publicEndpoints = {
            "/auth/token",
            "/auth/users",
            "/auth/refresh",
            "/auth/logout",
            "/identity/auth/.*",
            "/identity/users/registration",
            "/notification/email/send",
            "/file/media/download/.*"
    };

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Enter authentication filter....");

        if (isPublicEndpoint(exchange.getRequest())) {
            log.info("endpoint công khai");
            return chain.filter(exchange);
        }

        // Lấy Authorization header từ request
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeader)) {
            log.warn("Không có Authorization header, trả về lỗi Unauthenticated");
            return unauthenticated(exchange.getResponse(), "Missing Authorization header");
        }

        String token = authHeader.get(0);
        log.info("Token: {}", token);

        // Gửi token tới IdentityService để introspect (xác thực)
        return identityService.introspect(token).flatMap(introspectResponse -> {
            log.info("Kết quả introspect: {}", introspectResponse.getData());
            if (introspectResponse.getData().isValid()) {
                log.info("Token hợp lệ, chuyển tiếp request");
                return chain.filter(exchange);
            } else {
                log.warn("Token không hợp lệ, trả về lỗi Unauthenticated");
                return unauthenticated(exchange.getResponse(), "Invalid token");
            }
        }).onErrorResume(throwable -> {
            log.error("Lỗi khi introspect token: {}", throwable.getMessage(), throwable);
            return unauthenticated(exchange.getResponse(), "Error introspecting token: " + throwable.getMessage());
        });
    }

    private boolean isPublicEndpoint(ServerHttpRequest request) {
        return Arrays.stream(publicEndpoints)
                .anyMatch(s -> request.getURI().getPath().matches(apiPrefix + s));
    }

    Mono<Void> unauthenticated(ServerHttpResponse response) {
        return buildErrorResponse(response, HttpStatus.UNAUTHORIZED, "Unauthenticated");
    }

    Mono<Void> unauthenticated(ServerHttpResponse response, String message) {
        return buildErrorResponse(response, HttpStatus.UNAUTHORIZED, message);
    }

    Mono<Void> buildErrorResponse(ServerHttpResponse response, HttpStatus status, String message) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .message(message)
                .build();

        String body;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        response.setStatusCode(status);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
