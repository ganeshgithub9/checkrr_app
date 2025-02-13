package com.example.checkrr.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // Allow all requests
                .csrf(AbstractHttpConfigurer::disable)  // Disable CSRF (not recommended for production)
                .formLogin(AbstractHttpConfigurer::disable)  // Disable login form
                .httpBasic(AbstractHttpConfigurer::disable);  // Disable basic authentication

        return http.build();
    }
}

