package com.book.apigateway.service;


import com.book.apigateway.dto.ApiResponse;
import com.book.apigateway.dto.IntrospectRequest;
import com.book.apigateway.dto.IntrospectResponse;
import com.book.apigateway.repository.IdentityClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {

    IdentityClient identityClient ;

    public Mono<ApiResponse<IntrospectResponse>> introspect(String token) {
        return identityClient.introspect(IntrospectRequest.builder()
                .token(token).build()
        );
    }

}
