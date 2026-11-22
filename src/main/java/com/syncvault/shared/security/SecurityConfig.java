package com.syncvault.shared.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/workspace").permitAll()
                        .requestMatchers("/api/files").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/workspace/{workspace_id}/file"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/files/{objectKey}/content"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }
}
