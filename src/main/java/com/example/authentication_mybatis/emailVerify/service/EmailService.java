package com.example.authentication_mybatis.emailVerify.service;

import com.example.authentication_mybatis.emailVerify.dto.EmailDto;
import com.example.authentication_mybatis.emailVerify.dto.EmailRequest;
import com.example.authentication_mybatis.emailVerify.listener.EmailListener;
import com.example.authentication_mybatis.emailVerify.mapper.EmailMapper;
import com.example.authentication_mybatis.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailMapper emailMapper;
    private final ApplicationEventPublisher publisher;

    public String sendEmail(EmailRequest request){
        if (emailMapper.existsByEmail(request.getEmail()))
            throw new CustomException(HttpStatus.BAD_REQUEST, "Email already exists");
        SecureRandom random = new SecureRandom();
        Integer verifiedCode = 100000+ random.nextInt(900000);
        publisher.publishEvent(new EmailListener(request.getEmail(), verifiedCode));

        emailMapper.createCode(new EmailDto(request.getEmail(), verifiedCode));
        return "Please check your email to confirm verified code!";
    }

    public String verifyCode(EmailRequest request){
        EmailDto email = emailMapper.findByEmail(request.getEmail());
        if (email==null)
            throw new CustomException(HttpStatus.NOT_FOUND, "No exist email");
        else if (email.isSuccess())
            throw new CustomException(HttpStatus.BAD_REQUEST, "This email is already verified!");
        else if (email.getExpiration().before(Date.from(Instant.now())))
            throw new CustomException(HttpStatus.BAD_REQUEST, "Your code is already expired!");
        else if (!email.getCode().equals(request.getCode()))
            throw new CustomException(HttpStatus.BAD_REQUEST, "Wrong verified code!");
        emailMapper.verifyCode(request.getEmail());
        return "Verify successfully!";
    }

    public String resendCode(EmailRequest request){
        EmailDto email = emailMapper.findByEmail(request.getEmail());
        if (email==null)
            throw new CustomException(HttpStatus.NOT_FOUND, "No exist email");
        else if (email.isSuccess())
            throw new CustomException(HttpStatus.BAD_REQUEST, "This email is already verified!");
        SecureRandom random = new SecureRandom();
        Integer code = 100000+ random.nextInt(900000);
        publisher.publishEvent(new EmailListener(email.getEmail(), code));
        email.setCode(code);
        email.setExpiration(Date.from(Instant.now().plusSeconds(60*10)));
        emailMapper.resendCode(email);
        return "Please check your email again!";
    }
}
