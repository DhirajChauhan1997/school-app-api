package com.dc.schoolapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable())  // Disable CSRF for API calls
                .cors(cors -> cors.disable())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/actuator",
                                "/actuator/**",
                                "/actuator/beans",
                                "/auth",
                                "/auth/login",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html").permitAll() // Allow authentication endpoints
                         .anyExchange().authenticated() // Secure other endpoints
                );

        return http.build();
    }

}
