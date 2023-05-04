package com.mjy.econometrics.controller;


import com.mjy.econometrics.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/user")
    public OAuth2User user(@AuthenticationPrincipal OAuth2User oauth2User) {
        return oauth2User;
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(Authentication authentication) {
        String jwtToken = jwtUtil.generateToken(authentication);
        return "로그인 성공: " + jwtToken;
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