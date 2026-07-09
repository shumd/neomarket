package ru.shumilin.authservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import ru.shumilin.authservice.entity.RoleTypeEntity;
import ru.shumilin.authservice.entity.UsersEntity;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
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
        String login = "test login";
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
                .middleName("test middle name")
                .lastName("test last name")
                .login(login)
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

        Assertions.assertEquals(login, claims.getSubject());
        Assertions.assertEquals(permission, claims.get("role"));
    }
}
