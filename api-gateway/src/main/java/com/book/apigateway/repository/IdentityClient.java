package com.book.apigateway.repository;

import com.book.apigateway.dto.ApiResponse;
import com.book.apigateway.dto.IntrospectRequest;
import com.book.apigateway.dto.IntrospectResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

@HttpExchange
public interface IdentityClient {

    @PostExchange(url = "", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiResponse<IntrospectResponse>> introspect(@RequestBody IntrospectRequest request);

}
