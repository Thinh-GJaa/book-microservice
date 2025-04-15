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
import java.util.Map;


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
            "/identity/auth/.*",
            "/identity/users/registration",
            "/notification/email/send",
            "/file/media/download/.*"
    };

    // Mapping path với quyền tương ứng (1 quyền duy nhất cho mỗi user)
    Map<String, String> routeRoleMap = Map.of(
            "/user/all", "ROLE_ADMIN",
            "/user/profile", "ROLE_USER",
            "/product/delete", "ROLE_ADMIN",
            "/product/view", "ROLE_USER"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Enter authentication filter....");

        if (isPublicEndpoint(exchange.getRequest())) {
            return chain.filter(exchange);
        }

        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeader)) {
            return unauthenticated(exchange.getResponse());
        }

        String token = authHeader.get(0).replace("Bearer ", "");
        log.info("Token: {}", token);

        return identityService.introspect(token).flatMap(introspectResponse -> {
            if (!introspectResponse.getData().isValid()) {
                return unauthenticated(exchange.getResponse());
            }

//            String userRole = introspectResponse.getData().getRole();
            String userRole = "Admin";


            String requestPath = exchange.getRequest().getURI().getPath();
            log.info("Request path: {}", requestPath);

            for (String path : routeRoleMap.keySet()) {
                String fullPath = apiPrefix + path;
                if (requestPath.startsWith(fullPath)) {
                    String allowedRole = routeRoleMap.get(path);
                    boolean hasAccess = userRole.equals(allowedRole);
                    if (!hasAccess) {
                        return forbidden(exchange.getResponse());
                    }
                }
            }

            return chain.filter(exchange);
        }).onErrorResume(throwable -> unauthenticated(exchange.getResponse()));
    }

    private boolean isPublicEndpoint(ServerHttpRequest request) {
        return Arrays.stream(publicEndpoints)
                .anyMatch(s -> request.getURI().getPath().matches(apiPrefix + s));
    }

    Mono<Void> unauthenticated(ServerHttpResponse response) {
        return buildErrorResponse(response, HttpStatus.UNAUTHORIZED, "Unauthenticated");
    }

    Mono<Void> forbidden(ServerHttpResponse response) {
        return buildErrorResponse(response, HttpStatus.FORBIDDEN, "Forbidden - You don't have permission");
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
