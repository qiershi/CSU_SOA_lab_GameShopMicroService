package com.csu.userservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

/**
* @ProjectName: user-service
* @Title: JwtUtil
* @Package: com.csu.userservice.util
* @Description: JWT utils
* @author qiershi
* @date 2025/11/19 14:23
* @version V1.0
* Copyright (c) 2025, qiershi2006@h163.com All Rights Reserved.
*/
@Component
public class JwtUtil {
    // 使用足够长的密钥（至少64字符，512位）
    @Value("${jwt.secret:secureSecretKey1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // 24小时
    private long expirationTime;

    private SecretKey key;

    @PostConstruct
    public void init() {
        // 确保密钥长度足够（至少512位）
        if (secret.length() < 64) {
            throw new IllegalStateException("JWT secret key must be at least 512 bits (64 characters) long");
        }

        // 使用Base64编码的密钥更安全
        String base64EncodedSecret = Base64.getEncoder().encodeToString(secret.getBytes());
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64EncodedSecret));
    }

    public String generateToken(int userId) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new JwtException("Token expired", e);
        } catch (JwtException e) {
            throw new JwtException("Invalid token", e);
        }
    }

    public String extractSubject(String token) {
        return parseToken(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean validateToken(String token, String expectedSubject) {
        try {
            Claims claims = parseToken(token);
            return claims.getSubject().equals(expectedSubject) && !isTokenExpired(claims);
        } catch (JwtException e) {
            return false;
        }
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    public int getUserIdFromToken(String token) {
        try {
            return Integer.parseInt(extractSubject(token));
        } catch (NumberFormatException e) {
            throw new JwtException("Invalid user ID in token", e);
        }
    }

    public String extractUsername(String token) {
        return extractSubject(token);
    }

    // 获取剩余有效时间（毫秒）
    public long getRemainingTime(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }
}