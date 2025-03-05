package com.example.authentication_mybatis.token;

import com.example.authentication_mybatis.exception.CustomException;
import com.example.authentication_mybatis.token.dto.TokenRequest;
import com.example.authentication_mybatis.token.dto.TokenResponse;
import com.example.authentication_mybatis.user.dto.UserLogin;
import com.example.authentication_mybatis.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final TokenUtils tokenUtils;
    private final UserService userService;
    private final PasswordEncoder encoder;

    public TokenResponse getToken(TokenRequest request){
        UserLogin userLogin = (UserLogin) userService.loadUserByUsername(request.getUsername());
        log.info(userLogin.toString());
        if (!encoder.matches(request.getPassword(), userLogin.getPassword()))
            throw new CustomException(HttpStatus.CONFLICT, "Wrong password!");
        String token = tokenUtils.generateToken(request.getUsername());
        return TokenResponse.builder().token(token).build();
    }

    public boolean isTokenValidate(String token){
        return tokenUtils.validate(token);
    }
}
