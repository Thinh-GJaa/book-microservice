package com.book.bookservice.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfig {

        // Cấu hình riêng cho từng cache
        @Bean
        public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
                Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();

                cacheConfigs.put("categories", RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(10)));

                cacheConfigs.put("author", RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(10)));

                cacheConfigs.put("authors", RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(30)));

                cacheConfigs.put("product", RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(30)));

                cacheConfigs.put("products", RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(30)));

                cacheConfigs.put("products_by_category", RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(30)));

                cacheConfigs.put("products_by_author", RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(30)));

                return RedisCacheManager.builder(connectionFactory)
                                .withInitialCacheConfigurations(cacheConfigs)
                                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig())
                                .build();
        }

}
