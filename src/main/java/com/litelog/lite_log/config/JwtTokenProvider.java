package com.litelog.lite_log.config;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final long TOKEN_VALIDITY_HOURS = 1;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        byte[] keyBytes = Base64.getDecoder().decode(jwtProperties.getSecretKey());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(UserDetails userDetails) {
        return createToken(userDetails.getUsername());
    }

    public String createToken(String username) {
        LocalDateTime now = LocalDateTime.now();  // 현재 시간
        LocalDateTime expiry = now.plusHours(TOKEN_VALIDITY_HOURS);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expiry.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(key, SignatureAlgorithm.ES256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(token)
                .getBody().getSubject();
    }
}
