package com.example.demo.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * LESSON 8: Cache Configuration
 * Cấu hình Redis cache cho Spring Boot
 */
@Configuration
public class CacheConfig {
    
    /**
     * Redis Cache Manager với custom configuration
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // Default cache configuration
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)) // Default TTL: 10 minutes
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues(); // Don't cache null values
        
        // Custom cache configurations for different cache names
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // Students cache - 10 minutes TTL
        cacheConfigurations.put("students", defaultConfig
                .entryTtl(Duration.ofMinutes(10)));
        
        // Students list cache - 5 minutes TTL (more dynamic)
        cacheConfigurations.put("students-list", defaultConfig
                .entryTtl(Duration.ofMinutes(5)));
        
        // Search results cache - 3 minutes TTL (very dynamic)
        cacheConfigurations.put("students-search", defaultConfig
                .entryTtl(Duration.ofMinutes(3)));
        
        // Statistics cache - 30 minutes TTL (expensive operations)
        cacheConfigurations.put("students-stats", defaultConfig
                .entryTtl(Duration.ofMinutes(30)));
        
        // User profiles cache - 60 minutes TTL (rarely changed)
        cacheConfigurations.put("users", defaultConfig
                .entryTtl(Duration.ofMinutes(60)));
        
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
    
    /**
     * Redis Template for manual cache operations
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // Key serializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        
        // Value serializer - Use GenericJackson2JsonRedisSerializer for better compatibility
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();
        
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);
        
        template.afterPropertiesSet();
        return template;
    }
}