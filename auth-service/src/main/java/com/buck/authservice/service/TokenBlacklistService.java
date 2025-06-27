package com.buck.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void blacklistToken(String token, long expirationSeconds) {
        String key = "blacklist:" + token; // Sửa tại đây
        redisTemplate.opsForValue().set(key, "blacklisted", expirationSeconds, TimeUnit.SECONDS);
        System.out.println("Blacklisting token: " + token + " for " + expirationSeconds + " seconds");
    }

    public boolean isTokenBlacklisted(String token) {
        String key = "blacklist:" + token; // Đảm bảo key khớp với Redis
        boolean result = redisTemplate.hasKey(key);
        System.out.println("✅ Checking blacklist: " + key + " → " + result);
        return result;
    }
}