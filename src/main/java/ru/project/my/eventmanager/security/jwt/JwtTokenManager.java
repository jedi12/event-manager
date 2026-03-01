package ru.project.my.eventmanager.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.project.my.eventmanager.services.model.User;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenManager {
    private final SecretKey secretKey;
    private final int expTimeMinutes;

    public JwtTokenManager(@Value("${eventmanager.jwt-secret-key}") String key, @Value("${eventmanager.jwt-lifetime-minutes}") int expTimeMinutes) {
        this.secretKey = Keys.hmacShaKeyFor(key.getBytes());
        this.expTimeMinutes = expTimeMinutes;
    }

    public String generateJwt(User user) {
        return Jwts.builder()
                .subject(user.getLogin())
                .claim("role", user.getRole().name())
                .claim("userId", user.getId())
                .signWith(secretKey)
                .issuedAt(new Date())
                .expiration(DateUtils.addMinutes(new Date(), expTimeMinutes))
                .compact();
    }

    public String getLogin(String jwt) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getSubject();
    }
}
