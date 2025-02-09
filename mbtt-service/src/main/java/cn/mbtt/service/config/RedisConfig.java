package cn.mbtt.service.config;

//注释未使用的 Redis配置

//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.redis.RedisCacheManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//@Configuration
//@EnableCaching  // 启用缓存
//public class RedisConfig {
//
//    @Bean
//    public CacheManager cacheManager(RedisTemplate<String, Object> redisTemplate) {
//        // 配置缓存管理器
//        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
//
//        return RedisCacheManager.builder(redisTemplate.getConnectionFactory())
//                .cacheDefaults(cacheConfig)
//                .build();
//    }
//}
