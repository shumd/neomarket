package ru.shumilin.authservice.service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import java.time.Duration;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public class JwtBlackListServiceTest {
    @Mock
    private StringRedisTemplate redisTemplate;
    @Mock
    private ValueOperations<String, String> valueOperations;
    @Mock
    private Claims claims;
    @InjectMocks
    private JwtBlackListService jwtBlackListService;

    @Test
    public void addToBlackList_withValidToken_addToRedis() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(claims.getId()).thenReturn("jwt-id");
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() + 60000));
        jwtBlackListService.addToBlackList(claims);
        verify(valueOperations).set(eq("jwt:blacklist:jwt-id"), eq(""), any(Duration.class));
    }

    @Test
    public void addToBlackList_whenClaimsIsNull_throwIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> jwtBlackListService.addToBlackList(null));
        verifyNoInteractions(redisTemplate);
    }

    @Test
    public void isInBlackList_whenTokenExists_returnTrue() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(claims.getId()).thenReturn("jwt-id");
        when(valueOperations.get("jwt:blacklist:jwt-id")).thenReturn("");
        boolean result = jwtBlackListService.isInBlackList(claims);
        assertTrue(result);
    }

    @Test
    public void isInBlackList_whenTokenDoesNotExist_returnFalse() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(claims.getId()).thenReturn("jwt-id");
        when(valueOperations.get("jwt:blacklist:jwt-id")).thenReturn(null);
        boolean result = jwtBlackListService.isInBlackList(claims);
        assertFalse(result);
    }

    @Test
    public void isInBlackList_whenClaimsIsNull_throwIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> jwtBlackListService.isInBlackList(null));
        verifyNoInteractions(redisTemplate);
    }
}
