package com.mjy.econometrics.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${jwt.secret-key}")
    private String jwtSecretKey;

    private static String SECRET_KEY;

    @PostConstruct
    public void init() {
        SECRET_KEY = jwtSecretKey;
    }
    private static final int EXPIRATION_TIME = 3600 * 1000; // 1 hour

    public static String generateToken(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", oAuth2User.getAttribute("email"));
        claims.put("authorities", oAuth2User.getAuthorities());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(oAuth2User.getName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
