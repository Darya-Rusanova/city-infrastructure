package ru.isu.cityinfra.frontend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Временная генерация JWT на стороне фронтенда, пока Identity & Auth Service
 * не реализует настоящую регистрацию/логин. Токен подписывается тем же
 * JWT_SECRET, что и остальные сервисы, поэтому Utility/Billing/... его принимают.
 * Когда Auth Service будет готов, этот класс и форма логина должны быть заменены
 * на реальный вызов POST /auth/login к auth-service.
 */
@Component
public class JwtUtil {

    private final SecretKey signingKey;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String userId, String role) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(userId)
                .claim("role", role)
                .issuedAt(new Date(now))
                .expiration(new Date(now + 3600_000))
                .signWith(signingKey)
                .compact();
    }
}
