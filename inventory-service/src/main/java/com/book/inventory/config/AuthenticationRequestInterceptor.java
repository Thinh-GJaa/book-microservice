package com.book.inventory.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Feign RequestInterceptor dùng để tự động lấy header "Authorization" từ HTTP
 * request gốc
 * và thêm vào tất cả các request gửi đi qua FeignClient.
 * <p>
 */
@Slf4j
@Configuration
public class AuthenticationRequestInterceptor implements RequestInterceptor {
        @Override
        public void apply(RequestTemplate template) {
                ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                                .getRequestAttributes();

                var authHeader = servletRequestAttributes != null
                                ? servletRequestAttributes.getRequest().getHeader("Authorization")
                                : null;

                log.info("Header: {}", authHeader);
                if (StringUtils.hasText(authHeader))
                        template.header("Authorization", authHeader);
        }
}