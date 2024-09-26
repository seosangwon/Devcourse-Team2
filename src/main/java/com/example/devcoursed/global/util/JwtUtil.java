package com.example.devcoursed.global.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private static final String SECRET_KEY = "abcdefghijklmnopqrstuvwxyz1234567890abcdefghijklmnopqrstuvwxyz1234567890abcdefghijklmnopqrstuvwxyz1234567890";

    private static final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));


    public static String encode(Map<String, Object> data) {

        Claims claims = Jwts
                .claims()
                .add("data", data)
                .add("type", "access_token")
                .build();

        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1000 * 60 * 5); // 1초 X 60 X 5

        return Jwts.builder()
                .subject("NBE2_T2")
                .claims(claims)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();


    }

    public static Claims decode(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();


    }


}
