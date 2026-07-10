package ru.shumilin.authservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import ru.shumilin.authservice.exception.InvalidTokenException;
import ru.shumilin.authservice.model.entity.RoleTypeEntity;
import ru.shumilin.authservice.model.entity.UsersEntity;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

public class JwtServiceTest {
    private final JwtService jwtService = new JwtService();
    private final String secret = "CJW9ILNCXN/v6SUuS0ljtmYJhSxo0PmAvmDBAV5ZP4Y=";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

    @BeforeEach
    void init(){
        ReflectionTestUtils.setField(jwtService, "secret", secret);
        ReflectionTestUtils.setField(jwtService, "expiration", 1000000);

    }

    @Test
    void generateToken_withValidEntity_returnToken(){
        String email = "test_email@mail.ru";
        String permission = "test permission";

        RoleTypeEntity roleTypeEntity = RoleTypeEntity.builder()
                .id(1)
                .nameType("test permission name")
                .permissions(permission)
                .activity(true)
                .comment("test comment")
                .build();
        UsersEntity usersEntity = UsersEntity
                .builder()
                .id(UUID.randomUUID())
                .firstName("test first name")
                .lastName("test last name")
                .email(email)
                .hashPassword("test hash password")
                .roleType(roleTypeEntity)
                .bankDetail("test bank detail")
                .build();

        String token = jwtService.generateToken(usersEntity);

        var claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Assertions.assertEquals(email, claims.getSubject());
        Assertions.assertEquals(permission, claims.get("role"));
    }

    @Test
    void generateToken_withNullEntity_throwIllegalArgumentException(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> jwtService.generateToken(null));
    }

    @Test
    void validateToken_withValidToken_returnTrue(){
        Assertions.assertTrue(jwtService.validateToken(
                getToken(null, null)));
    }

    @Test
    void validateToken_withInvalidExpiration_returnFalse(){
        Assertions.assertFalse(jwtService.validateToken(
                getToken(new Date(System.currentTimeMillis() - 1000), null)));
    }

    @Test
    void validateToken_withInvalidSecret_returnFalse(){
        String invalidSecret = "CJW9ILNCCC/v6SUuS0ljtmYJhSxo0PmAvmDBAV5ZP4Y=";
        Assertions.assertFalse(jwtService.validateToken(
                getToken(null, Keys.hmacShaKeyFor(invalidSecret.getBytes(StandardCharsets.UTF_8)))));
    }

    @Test
    void validateToken_withNullToken_returnFalse(){
        Assertions.assertFalse(jwtService.validateToken(null));
    }

    @Test
    void validateToken_withBlankToken_returnFalse(){
        Assertions.assertFalse(jwtService.validateToken("    "));
    }

    @Test
    void getClaims_withValidToken_returnClaims(){
        String token = getToken(null, null);
        Claims excepted = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Assertions.assertEquals(excepted, jwtService.getClaims(token));
    }

    @Test
    void getClaims_withNullToken_throwInvalidTokenException(){
        Assertions.assertThrows(InvalidTokenException.class,
                () -> jwtService.getClaims(null));
    }

    @Test
    void getClaims_withBlankToken_throwInvalidTokenException(){
        Assertions.assertThrows(InvalidTokenException.class,
                () -> jwtService.getClaims("      "));
    }

    @Test
    void getClaims_withInvalidExpiration_throwInvalidTokenException(){
        Assertions.assertThrows(InvalidTokenException.class,
                () -> jwtService.getClaims(
                        getToken(
                                new Date(System.currentTimeMillis() - 1000),
                                null)));
    }

    @Test
    void getClaims_withInvalidSecret_throwInvalidTokenException(){
        String invalidSecret = "CJW9ILNCCC/v6SUuS0ljtmYJhSxo0PmAvmDBAV5ZP4Y=";

        Assertions.assertThrows(InvalidTokenException.class,
                () -> jwtService.getClaims(
                        getToken(
                                null,
                                Keys.hmacShaKeyFor(invalidSecret.getBytes(StandardCharsets.UTF_8)))));
    }

    private String getToken(Date expiration, SecretKey secretKey){
        SecretKey key = secretKey == null ?
                Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)) : secretKey;
        Date expDate = expiration == null ?
                new Date(System.currentTimeMillis() + 100000) : expiration;

        return Jwts.builder()
                .subject("test subject")
                .claim("role", "test role permissions")
                .issuedAt(new Date())
                .expiration(expDate)
                .signWith(key)
                .compact();
    }
}
