package com.mjy.econometrics.controller;


import com.mjy.econometrics.config.JwtUtil;
import com.mjy.econometrics.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final JwtUtil jwtUtil;

    public UserController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/getUser")
    public OAuth2User user(@AuthenticationPrincipal OAuth2User oauth2User) {
        return oauth2User;
    }

    @GetMapping("/loginSuccess")
    public ResponseEntity<?> loginSuccess(Authentication authentication) {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String jwtToken = jwtUtil.generateToken(authentication);

        // 사용자 정보와 JWT 토큰을 함께 반환
        return ResponseEntity.ok(new LoginResponse(oAuth2User.getAttributes(), jwtToken));
    }

    @GetMapping("/loginFailure")
    public String loginFailure() {
        return "로그인 실패";
    }

    // 로그아웃 엔드포인트 추가
    @PostMapping("/logout")
    public String logout() {
        return "로그아웃";
    }
}