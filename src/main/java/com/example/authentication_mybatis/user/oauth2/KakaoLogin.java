package com.example.authentication_mybatis.user.oauth2;

import com.example.authentication_mybatis.exception.CustomException;
import com.example.authentication_mybatis.user.dto.UserDto;
import com.example.authentication_mybatis.user.dto.UserLogin;
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

@Service
@RequiredArgsConstructor
public class KakaoLogin extends DefaultOAuth2UserService {
    private final UserMapper userMapper;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> map = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) map.get("profile");
        String username = profile.get("nickname").toString();
        String email = map.get("email").toString();
        String name = map.get("name").toString();

        UserLogin userLogin;
        if (userMapper.existsByUsername(username))
            userLogin = userMapper.findByUsername(username);
        else if (userMapper.existsByEmail(email))
            userLogin = userMapper.findByEmail(email);
        else{
            userLogin = UserLogin.builder()
                    .username(username)
                    .email(email)
                    .name(name)
                    .authority("ROLE_USER").build();
            userMapper.userInfo(userLogin);
            UserDto user = UserDto.builder()
                    .username(username)
                    .email(email)
                    .name(name)
                    .avatar("/static/visual/user.png")
                    .build();
            userMapper.createUser(user);
        }

        return new DefaultOAuth2User(userLogin.getAuthorities(), userLogin.getAttributes(), "username");
    }
}
