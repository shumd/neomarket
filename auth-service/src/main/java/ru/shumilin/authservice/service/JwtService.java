package ru.shumilin.authservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.shumilin.authservice.exception.InvalidTokenException;
import ru.shumilin.authservice.exception.TokenInBlackListException;
import ru.shumilin.authservice.model.entity.UsersEntity;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {
    private final JwtBlackListService blackListService;

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration}")
    private long expiration;

    public String generateToken(UsersEntity usersEntity){
        if (usersEntity == null) throw new IllegalArgumentException("Users entity can not be null");

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(usersEntity.getEmail())
                .claim("role", usersEntity.getRoleType().getPermissions())
                .id(UUID.randomUUID().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token){
        try{
            if (token == null || token.isBlank())
                throw new IllegalArgumentException("Token cant be blank");

            log.info("Trying to parse token");

            if(blackListService.isInBlackList(getClaims(token)))
                throw new TokenInBlackListException();

            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException | TokenInBlackListException | InvalidTokenException e){
            log.warn("Invalid token, throw {}", e.getMessage());
            return false;
        }
    }

    public Claims getClaims(String token) {
        try {
            if (token == null || token.isBlank())
                throw new IllegalArgumentException("Token cant be blank");
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException();
        }
    }

    public void addToBlackList(String token){
        blackListService.addToBlackList(getClaims(token));
    }
}
