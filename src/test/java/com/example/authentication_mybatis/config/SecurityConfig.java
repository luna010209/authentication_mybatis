package com.example.authentication_mybatis.config;

import com.example.authentication_mybatis.exception.CustomException;
import com.example.authentication_mybatis.token.TokenHandler;
import com.example.authentication_mybatis.token.TokenOAuth2;
import com.example.authentication_mybatis.token.TokenUtils;
import com.example.authentication_mybatis.user.oauth2.KakaoLogin;
import com.example.authentication_mybatis.user.oauth2.NaverLogin;
import com.example.authentication_mybatis.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenUtils tokenUtils;
    private final UserService userService;
    private final NaverLogin naverLogin;
    private final KakaoLogin kakaoLogin;
    private final TokenOAuth2 tokenOAuth2;
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
                )
                .oauth2Login(oauth2->oauth2
                        .userInfoEndpoint(userInfo->userInfo
                                .userService(oAuth2Provider()))
                        .successHandler(tokenOAuth2)
                        .permitAll());
        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2Provider(){
        return userRequest -> {
            String registerId = userRequest.getClientRegistration().getRegistrationId();
            if ("naver".equals(registerId)){
                return naverLogin.loadUser(userRequest);
            } else if ("kakao".equals(registerId)) {
                return kakaoLogin.loadUser(userRequest);
            } else{
                throw new CustomException(HttpStatus.BAD_REQUEST, "No exist this provider");
            }
        };
    }
}
