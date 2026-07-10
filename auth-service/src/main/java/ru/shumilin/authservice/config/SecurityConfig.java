package ru.shumilin.authservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.shumilin.authservice.security.CustomAuthenticationEntryPoint;
import ru.shumilin.authservice.security.JwtAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   JwtAuthenticationFilter jwtAuthenticationFilter,
                                                   CustomAuthenticationEntryPoint customAuthenticationEntryPoint){
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(e ->
                        e.authenticationEntryPoint(customAuthenticationEntryPoint))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeHttpRequest ->
                        authorizeHttpRequest
                                .requestMatchers("/auth/logout",
                                        "/users/me/bank-details").authenticated()
                                .requestMatchers("/auth/login",
                                        "/auth/register").permitAll()
                                .requestMatchers("/swagger-ui/**",
                                        "/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
