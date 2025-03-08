package com.example.authentication_mybatis.token;

import com.example.authentication_mybatis.token.dto.TokenRequest;
import com.example.authentication_mybatis.token.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.apache.el.parser.Token;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("token")
public class TokenController {
    private final TokenService tokenService;
    @PostMapping
    public TokenResponse getToken(@RequestBody TokenRequest request){
        return tokenService.getToken(request);
    }
    @GetMapping("/{token}")
    public boolean isTokenValidate(@PathVariable("token") String token){
        return tokenService.isTokenValidate(token);
    }
    @GetMapping("oauth2")
    public TokenResponse tokenOAuth2(){
        return tokenService.getTokenOAuth2();
    }
}
