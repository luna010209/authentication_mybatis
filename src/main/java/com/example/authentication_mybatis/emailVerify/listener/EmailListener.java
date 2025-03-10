package com.example.authentication_mybatis.emailVerify.listener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class EmailListener extends ApplicationEvent {
    private String email;
    private Integer code;

    public EmailListener(String email, Integer code) {
        super(email);
        this.email = email;
        this.code = code;
    }
}
