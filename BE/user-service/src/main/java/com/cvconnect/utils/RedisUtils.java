package com.cvconnect.utils;

import nmquan.commonlib.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisUtils {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Value("${redis.token-key}")
    private String REDIS_TOKEN_KEY;

    public <T> void saveObject(String key, T object, int duration) {
        Duration ttl = Duration.ofSeconds(duration);
        redisTemplate.opsForValue().set(key, object, ttl);
    }

    public void deleteByKey(String key) {
        redisTemplate.delete(key);
    }

    public <T> T getObject(String key, Class<T> clazz) {
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj == null) return null;
        return ObjectMapperUtils.convertToObject(obj, clazz);
    }

    public String getTokenKey(String token) {
        return REDIS_TOKEN_KEY + ":" + token;
    }
}
