package com.litelog.lite_log.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private PrivateKey privateKey;
    private PublicKey publicKey;
    private static final long TOKEN_VALIDITY_HOURS = 1;

    public JwtTokenProvider() throws Exception {
        this.privateKey = loadPrivateKey("keys/private_key.pem");
        this.publicKey = loadPublicKey("keys/public_key.pem");
    }

    private PrivateKey loadPrivateKey(String resourcePath) throws Exception {
        byte[] keyBytes = readKeyFile(resourcePath);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    private PublicKey loadPublicKey(String resourcePath) throws Exception {
        byte[] keyBytes = readKeyFile(resourcePath);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    private byte[] readKeyFile(String resourcePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        String keyContent = new String(Files.readAllBytes(resource.getFile().toPath()));
        keyContent = keyContent.replaceAll("-----BEGIN .* KEY-----", "")
                .replaceAll("-----END .* KEY-----", "")
                .replaceAll("\\s+", "");
        return Base64.getDecoder().decode(keyContent);
    }

    public String createToken(UserDetails userDetails) {
        return createToken(userDetails.getUsername());
    }

    public String createToken(String username) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiry = now.plusHours(TOKEN_VALIDITY_HOURS);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expiry.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
