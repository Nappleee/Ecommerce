package com.example.api_gateway.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @PostConstruct
    public void init() {
        System.out.println("✅ SecurityConfig LOADED!!!");
    }
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        System.out.println("✅ SecurityConfig loaded");

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/auth/**").permitAll()
                        .pathMatchers("/api/products/**").permitAll()
                        .pathMatchers("/api/users/**").permitAll()
                        .anyExchange().permitAll() // hoặc .authenticated() nếu muốn
                )
                .build();
    }
}
