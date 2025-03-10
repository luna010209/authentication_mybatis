package com.example.authentication_mybatis.emailVerify.listener;

import com.example.authentication_mybatis.exception.CustomException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class EmailListenerEvent implements ApplicationListener<EmailListener> {
    private final JavaMailSender javaMailSender;
    @Override
    public void onApplicationEvent(EmailListener event) {
        try{
            String subject = "Email verification for my website";
            String senderName = "Luna Do";
            String content =
                    "<p>Thank you for your registration with us.<br>" +
                    "This is a verified code</p>"+
                    "<b>" + event.getCode()+"</b>" +
                    "<p>Thank you so much ^^<br><br>"+
                    "Your sincerely, <br> Luna Do";
            MimeMessage message = javaMailSender.createMimeMessage();
            var messageHelper = new MimeMessageHelper(message);
            messageHelper.setFrom("liendhhha140217@gmail.com", senderName);
            messageHelper.setTo(event.getEmail());
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);
            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e){
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }
}
