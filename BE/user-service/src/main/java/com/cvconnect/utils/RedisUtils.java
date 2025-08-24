package com.cvconnect.utils;

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

    public Object getObjectByKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public String getFreshTokenKey(String token) {
        return REDIS_TOKEN_KEY + ":" + token;
    }
}
