package com.example.fintech.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomJwtUtil {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private Claims extractAllClaims(String token) {
        String pureToken = token.replace("Bearer ", "");

        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(pureToken)
                .getBody();
    }


    // userId 추출
    public Long getUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("id", Long.class);
    }

    // Role 추출
    public String getRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }
}