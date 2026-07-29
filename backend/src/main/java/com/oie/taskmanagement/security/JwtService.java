package com.oie.taskmanagement.security;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import java.util.Date;

@Component
public class JwtService {
    private final SecretKey secretKey;
    private final long expirationMs;
    public JwtService(@Value("${jwt.secret}") String secret,
    @Value("${jwt.expiration-ms}") long expirationMs){

        this.expirationMs = expirationMs;

        byte[] bytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(bytes);
    }
    public String generateToken(String subject) {
        long nowMs = System.currentTimeMillis();

        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date(nowMs))
                .expiration(new Date(nowMs + expirationMs))
                .signWith(secretKey)
                .compact();
    }
}
