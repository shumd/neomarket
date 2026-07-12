package ru.shumilin.authservice.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtBlackListService {
    private final StringRedisTemplate redisTemplate;

    private final static String PREFIX = "jwt:blacklist:";

    public void addToBlackList(Claims claims){
        if(claims == null)
            throw new IllegalArgumentException("Claims cant be null");

        String jti = claims.getId();

        Duration duration = Duration.between(
                Instant.now(),
                claims.getExpiration().toInstant()
        );

        redisTemplate.opsForValue()
                .set(
                        PREFIX + jti,
                        "",
                        duration
                );
    }

    public boolean isInBlackList(Claims claims){
        if(claims == null)
            throw new IllegalArgumentException("Claims cant be null");

        String key = PREFIX + claims.getId();

        return redisTemplate.opsForValue()
                .get(key) != null;
    }
}
