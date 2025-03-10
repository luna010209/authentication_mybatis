package com.example.authentication_mybatis.user.userLogin;

import com.example.authentication_mybatis.exception.CustomException;
import com.example.authentication_mybatis.user.dto.UserDto;
import com.example.authentication_mybatis.user.dto.UserLogin;
import com.example.authentication_mybatis.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserComponent {
    private final UserMapper userMapper;
    public UserDto userLogin(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto user = userMapper.userDto(username);
        if (user==null)
            throw new CustomException(HttpStatus.NOT_FOUND, "No exist user login");
        return user;
    }
}
