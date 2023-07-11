package com.segolin.client.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private static final String[] WHITE_LIST_URLS = {"/hello", "/register", "/", "/verifyRegistration"};

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .authorizeHttpRequests((request) -> request
                        .requestMatchers(WHITE_LIST_URLS).permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling()
                .accessDeniedHandler((request, response, ex) -> {
                    System.out.println("Access denied" + ex.getMessage());
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                });

        return http.build();
    }

}
