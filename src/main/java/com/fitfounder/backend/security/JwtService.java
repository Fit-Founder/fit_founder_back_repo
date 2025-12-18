package com.fitfounder.backend.security;

import com.fitfounder.backend.config.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Component
public class JwtService {
    private final JwtProperties properties;
    private final Key signingKey;

    public JwtService(JwtProperties properties) {
        this.properties = properties;
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(properties.getSecret()));
    }

    public String generateAccessToken(String userId) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(properties.getAccessTokenTtlSeconds(), ChronoUnit.SECONDS);
        return buildToken(userId, "access", null, Date.from(now), Date.from(expiresAt));
    }

    public String generateRefreshToken(String sessionId, String userId) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(properties.getRefreshTokenTtlSeconds(), ChronoUnit.SECONDS);
        return buildToken(userId, "refresh", Map.of("sid", sessionId), Date.from(now), Date.from(expiresAt));
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .requireAudience(properties.getAudience())
                .requireIssuer(properties.getIssuer())
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token);
    }

    public Claims parseRefreshClaims(String token) {
        Claims claims = parseToken(token).getBody();
        if (!"refresh".equals(claims.get("typ"))) {
            throw new JwtException("Invalid token type");
        }
        return claims;
    }

    private String buildToken(String userId, String type, Map<String, Object> additionalClaims, Date issuedAt, Date expiresAt) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(userId)
                .setAudience(properties.getAudience())
                .setIssuer(properties.getIssuer())
                .setIssuedAt(issuedAt)
                .setExpiration(expiresAt)
                .claim("typ", type);

        if (additionalClaims != null) {
            builder.addClaims(additionalClaims);
        }

        return builder
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
