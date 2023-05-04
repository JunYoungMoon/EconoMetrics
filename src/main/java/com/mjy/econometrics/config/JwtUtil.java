package com.mjy.econometrics.config;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    public String generateToken(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", oAuth2User.getAttribute("email"));
        claims.put("authorities", oAuth2User.getAuthorities());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(oAuth2User.getName())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(Duration.ofHours(1))))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
