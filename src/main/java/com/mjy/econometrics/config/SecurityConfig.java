package com.mjy.econometrics.config;


import com.mjy.econometrics.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/login**", "/css/**", "/js/**", "/images/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .defaultSuccessUrl("/loginSuccess")
                .failureUrl("/loginFailure")
                .and()
                .logout()
                .logoutUrl("/logout") // 사용자가 로그아웃을 요청하는 엔드포인트
                .logoutSuccessUrl("/") // 로그아웃이 성공한 후 이동할 페이지
                .invalidateHttpSession(true) // 로그아웃 시 세션 무효화
                .clearAuthentication(true) // 인증 정보를 지움
                .deleteCookies("JSESSIONID") // 쿠키 삭제
                .addLogoutHandler(new LogoutHandler() {
                    @Override
                    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                        // OAuth2 로그아웃 처리 로직
                        String logoutUrl = UriComponentsBuilder
                                .fromHttpUrl("https://accounts.google.com/logout")
                                .queryParam("redirect_uri", "http://localhost:8085") // 로그아웃 후 리다이렉션될 URL
                                .build().encode().toUriString();

                        response.setHeader("Location", logoutUrl);
                        response.setStatus(HttpStatus.FOUND.value());
                    }
                });

        return http.build();
    }
}




