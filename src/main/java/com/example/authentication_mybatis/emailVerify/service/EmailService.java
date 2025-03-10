package com.example.authentication_mybatis.emailVerify.service;

import com.example.authentication_mybatis.emailVerify.dto.EmailDto;
import com.example.authentication_mybatis.emailVerify.dto.EmailRequest;
import com.example.authentication_mybatis.emailVerify.listener.EmailListener;
import com.example.authentication_mybatis.emailVerify.mapper.EmailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailMapper emailMapper;
    private final ApplicationEventPublisher publisher;

    public String sendEmail(EmailRequest request){
        SecureRandom random = new SecureRandom();
        Integer verifiedCode = 100000+ random.nextInt(900000);
        publisher.publishEvent(new EmailListener(request.getEmail(), verifiedCode));

        emailMapper.createCode(new EmailDto(request.getEmail(), verifiedCode));
        return "Please check your email to confirm verified code!";
    }
}
