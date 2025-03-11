package com.example.authentication_mybatis.emailVerify;

import com.example.authentication_mybatis.emailVerify.dto.EmailDto;
import com.example.authentication_mybatis.emailVerify.dto.EmailRequest;
import com.example.authentication_mybatis.emailVerify.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("email")
public class EmailController {
    private final EmailService emailService;
    @PostMapping("sending")
    public String sendCode(@RequestBody EmailRequest request){
        return emailService.sendEmail(request);
    }
    @PostMapping
    public String verifyCode(@RequestBody EmailRequest request){
        return emailService.verifyCode(request);
    }
}
