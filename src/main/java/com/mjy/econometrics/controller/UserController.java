package com.mjy.econometrics.controller;


import com.mjy.econometrics.config.JwtUtil;
import com.mjy.econometrics.dto.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    @GetMapping("/custom-logout")
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication != null && authentication.isAuthenticated()) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();

            if ("google".equals(provider)) {
                // 구글 세션 종료
                String logoutUrl = "https://accounts.google.com/logout?continue=http://localhost:8085"; // 로그아웃 후 리다이렉트할 URL
                String idToken = (String) oauth2User.getAttribute("id_token");
                String redirectUrl = logoutUrl + "&id_token_hint=" + idToken;
                response.sendRedirect(redirectUrl);
                return;
            }
        }

        // 기본 로그아웃 처리
        new SecurityContextLogoutHandler().logout(request, response, authentication);
    }
}