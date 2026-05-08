package com.rbf.product.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AutoConfiguration
@ConditionalOnClass(SecurityFilterChain.class)
@EnableMethodSecurity
public class SharedSecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SharedJwtUtil sharedJwtUtil(ObjectMapper objectMapper) {
        return new SharedJwtUtil(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public SharedJwtAuthenticationFilter sharedJwtAuthenticationFilter(SharedJwtUtil jwtUtil) {
        return new SharedJwtAuthenticationFilter(jwtUtil);
    }

    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain sharedSecurityFilterChain(HttpSecurity http, SharedJwtAuthenticationFilter filter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
