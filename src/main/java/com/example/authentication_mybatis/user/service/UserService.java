package com.example.authentication_mybatis.user.service;

import com.example.authentication_mybatis.exception.CustomException;
import com.example.authentication_mybatis.user.dto.UserDto;
import com.example.authentication_mybatis.user.dto.UserRequest;
import com.example.authentication_mybatis.user.mapper.UserMapper;
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
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
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
        userMapper.createUser(dto);
        return dto;
    }
}
