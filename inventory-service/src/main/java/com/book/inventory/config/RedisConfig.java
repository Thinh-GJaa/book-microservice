package com.book.inventory.config;

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

                cacheConfigs.put("supplier", RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(10)));

                cacheConfigs.put("suppliers", RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(10)));

                cacheConfigs.put("warehouse", RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(10)));

                cacheConfigs.put("warehouses", RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(10)));

                cacheConfigs.put("purchaseOrder", RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(10)));

                cacheConfigs.put("purchaseOrders", RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(10)));

                cacheConfigs.put("stockOut", RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(10)));

                cacheConfigs.put("stockOuts", RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(10)));
                return RedisCacheManager.builder(connectionFactory)
                                .withInitialCacheConfigurations(cacheConfigs)
                                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig())
                                .build();
        }

}
