package com.example.devcoursed.global.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${spring.jwt.secret_key}")
    private String SECRET_KEY ;

    private  SecretKey key;

    @PostConstruct
    public void init() {
        key= Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }


    public  String encodeAccessToken(long minute, Map<String, Object> data) {

        Claims claims = Jwts
                .claims()
                .add("data", data)
                .add("type", "access_token")
                .build();

        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1000 * 60 * minute); // 1초 X 60 X

        return Jwts.builder()
                .subject("NBE2_T2")
                .claims(claims)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();


    }
    public  String encodeRefreshToken(long minute,Map<String, Object> data) {

        Claims claims = Jwts
                .claims()
                .add("data", data)
                .add("type", "refresh_token")
                .build();

        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1000 * 60 * minute); // 1초 X 60 X

        return Jwts.builder()
                .subject("NBE2_T2")
                .claims(claims)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();


    }

    public  Claims decode(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();


    }


}
