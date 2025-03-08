package com.example.authentication_mybatis.user.mapper;

import com.example.authentication_mybatis.user.dto.UserDto;
import com.example.authentication_mybatis.user.dto.UserLogin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void createUser(UserDto dto);
    void userInfo(UserLogin login);
//    UserLogin userLogin(String username);
    UserLogin findByUsername(String username);
    UserLogin findByEmail(String email);
}
