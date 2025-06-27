package com.buck.authservice.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    public RefreshTokenService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";

    public void store(String refreshToken, String username, long durationMs) {
        redisTemplate.opsForValue().set(
                REFRESH_TOKEN_PREFIX + refreshToken,
                username,
                Duration.ofMillis(durationMs)
        );
    }

    public boolean isValid(String refreshToken) {
        return redisTemplate.hasKey(REFRESH_TOKEN_PREFIX + refreshToken);
    }

    public String getUsername(String refreshToken) {
        return redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + refreshToken);
    }

    public void revoke(String refreshToken) {
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + refreshToken);
    }
}
