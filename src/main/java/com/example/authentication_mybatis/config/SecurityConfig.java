package com.example.authentication_mybatis.config;

import com.example.authentication_mybatis.token.TokenHandler;
import com.example.authentication_mybatis.token.TokenUtils;
import com.example.authentication_mybatis.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenUtils tokenUtils;
    private final UserService userService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->{
//                    auth.requestMatchers("").authenticated();
                    auth.anyRequest().permitAll();
                })
                .addFilterBefore(
                        new TokenHandler(tokenUtils, userService),
                        AuthorizationFilter.class
                );
        return http.build();
    }
}
