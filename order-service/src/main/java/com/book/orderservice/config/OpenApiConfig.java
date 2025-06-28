package com.book.orderservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class OpenApiConfig {
    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        log.info("Configuring OpenAPI for Order Service");
        return new OpenAPI()
                .info(new Info()
                        .title("Order Service API")
                        .description("API for order management. Secured with JWT.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Thinh_GJaa")
                                .email("xthinh04052002@gmail.com")
                                .url("https://github.com/Thinh-GJaa"))
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .schemaRequirement(SECURITY_SCHEME_NAME, new SecurityScheme()
                        .name(SECURITY_SCHEME_NAME)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER)
                        .description("""
                                Use Bearer JWT authentication:
                                - Add header: `Authorization: Bearer <token>`
                                - Token is returned after successful login.
                                - Most endpoints require a valid JWT token.
                                """));
    }
} 