package com.example.authentication_mybatis.emailVerify.service;

import com.example.authentication_mybatis.emailVerify.mapper.EmailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailMapper emailMapper;

}
