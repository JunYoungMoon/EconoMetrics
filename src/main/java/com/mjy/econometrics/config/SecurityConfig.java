package com.mjy.econometrics.config;


import com.mjy.econometrics.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebSecurityConfigurer<WebSecurity> {

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Override
    public void init(WebSecurity web) throws Exception {

    }

    @Override
    public void configure(WebSecurity web) throws Exception {


    }
}





