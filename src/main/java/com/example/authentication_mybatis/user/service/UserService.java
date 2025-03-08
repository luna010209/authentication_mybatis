package com.example.authentication_mybatis.user.service;

import com.example.authentication_mybatis.exception.CustomException;
import com.example.authentication_mybatis.user.dto.UserDto;
import com.example.authentication_mybatis.user.dto.UserLogin;
import com.example.authentication_mybatis.user.dto.UserRequest;
import com.example.authentication_mybatis.user.mapper.UserMapper;
import com.example.authentication_mybatis.user.userLogin.UserComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;
    private final UserComponent userComponent;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserLogin userLogin = userMapper.findByUsername(username);
        if (userLogin==null)
            throw new CustomException(HttpStatus.NOT_FOUND, "Username does not exist!!");
        return userLogin;
    }

    public UserDto newUser(UserRequest request){
        if (userMapper.existsByUsername(request.getUsername()))
            throw new CustomException(HttpStatus.BAD_REQUEST, "Username already exists!");
        else if (userMapper.existsByEmail(request.getEmail()))
            throw new CustomException(HttpStatus.BAD_REQUEST, "Email already exists!");
        else if (!request.getPassword().equals(request.getPwConfirm()))
            throw new CustomException(HttpStatus.CONFLICT, "Password and password confirm do not match!");
        UserDto dto = UserDto.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .name(request.getName())
                .build();
        UserLogin userLogin = UserLogin.builder()
                .username(request.getUsername())
                .hashedPw(encoder.encode(request.getPassword()))
                .email(request.getEmail())
                .name(request.getName())
                .authority("ROLE_USER")
                .build();
        userMapper.createUser(dto);
        userMapper.userInfo(userLogin);
        return dto;
    }

    public UserDto userLogin(){
        return userComponent.userLogin();
    }
}
