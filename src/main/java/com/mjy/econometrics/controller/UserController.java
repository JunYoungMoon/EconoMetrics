package com.mjy.econometrics.controller;


import com.mjy.econometrics.config.JwtUtil;
import com.mjy.econometrics.dto.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final JwtUtil jwtUtil;

    public UserController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/getUser")
    public ResponseEntity<?> user(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String jwtToken = jwtUtil.generateToken(authentication);

        // 사용자 정보와 JWT 토큰을 함께 반환
        return ResponseEntity.ok(new AuthResponse(oAuth2User.getAttributes(), jwtToken));
    }

    @GetMapping("/loginFailure")
    public String loginFailure() {
        return "로그인 실패";
    }

    // 로그아웃 엔드포인트 추가
    @GetMapping("/logout")
    public String logout() {
        return "로그아웃";
    }
}