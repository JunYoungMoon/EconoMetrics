package com.mjy.econometrics.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/login**", "/css/**", "/js/**", "/images/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .and()
                .logout().logoutUrl("/custom-logout");  // custom logout url

        return http.build();
    }
}





