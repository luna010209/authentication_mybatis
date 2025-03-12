package com.example.authentication_mybatis.emailVerify.mapper;

import com.example.authentication_mybatis.emailVerify.dto.EmailDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmailMapper {
    void createCode(EmailDto emailDto);
    EmailDto findByEmail(String email);
    boolean existsByEmail(String email);
    void verifyCode(String email);
    void resendCode(EmailDto emailDto);
}
