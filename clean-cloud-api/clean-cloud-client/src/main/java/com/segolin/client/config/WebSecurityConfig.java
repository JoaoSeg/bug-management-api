package com.segolin.client.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.*;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private static final String TESTER = "/api/bug/tester";
    private static final String DEVELOPER = "/api/bug/developer";
    private static final String MANAGER = "/api/bug/manager";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((request)  -> {
                    request.requestMatchers("/").permitAll();
                    request.requestMatchers(HttpMethod.POST, "/register").permitAll();
                    request.requestMatchers(HttpMethod.POST, TESTER).hasAnyAuthority("TESTER");
                    request.requestMatchers(HttpMethod.PUT, TESTER).hasAnyAuthority("TESTER");
                    request.requestMatchers(HttpMethod.DELETE, TESTER).hasAnyAuthority("TESTER");
                    request.requestMatchers(HttpMethod.GET, DEVELOPER).hasAnyAuthority("DEVELOPER");
                    request.requestMatchers(HttpMethod.GET, "/api/bug/developer/listbugs").hasAnyAuthority("DEVELOPER");
                    request.requestMatchers(HttpMethod.PUT, DEVELOPER).hasAnyAuthority("DEVELOPER");
                    request.requestMatchers(HttpMethod.GET, MANAGER).hasAnyAuthority("MANAGER");
                    request.requestMatchers(HttpMethod.GET, "/verifyRegistration*").permitAll();
                    request.requestMatchers(HttpMethod.GET, "/secured/user").hasAnyAuthority("USER");
                    request.anyRequest().authenticated();
                })
                .oauth2Login(withDefaults())
                .formLogin(withDefaults())
                .csrf(CsrfConfigurer::disable)
                .cors(CorsConfigurer::disable);

        return http.build();
    }

}
