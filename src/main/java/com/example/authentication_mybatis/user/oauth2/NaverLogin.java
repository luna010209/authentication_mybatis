package com.example.authentication_mybatis.user.oauth2;

import com.example.authentication_mybatis.exception.CustomException;
import com.example.authentication_mybatis.user.dto.UserDto;
import com.example.authentication_mybatis.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NaverLogin extends DefaultOAuth2UserService {
    private final UserMapper userMapper;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> responseMap = oAuth2User.getAttribute("response");
        String username = responseMap.get("nickname").toString();
        String name = responseMap.get("name").toString();
        String email = responseMap.get("email").toString();

        if (userMapper.existsByUsername(username))
            throw new CustomException(HttpStatus.BAD_REQUEST, "Username already exists");
        else if (userMapper.existsByEmail(email))
            throw new CustomException(HttpStatus.BAD_REQUEST, "Email already exists");
        UserDto user= UserDto.builder()
                .username(username)
                .name(name)
                .email(email)
                .authority("ROLE_USER")
                .build();
        userMapper.createUser(user);
        return new DefaultOAuth2User(user.getAuthorities(), user.getAttributes(), "username");
    }
}
