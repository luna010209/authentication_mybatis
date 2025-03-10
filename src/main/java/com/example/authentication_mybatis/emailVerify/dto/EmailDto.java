package com.example.authentication_mybatis.emailVerify.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
public class EmailDto {
    private String email;
    private Integer code;
    private boolean isSuccess;
    private Date expiration;

    public EmailDto(String email, Integer code){
        this.email=email;
        this.code=code;
        this.isSuccess=false;
        this.expiration=getCodeExpiration();
    }

    private Date getCodeExpiration(){
        Instant now = Instant.now();
        return Date.from(now.plusSeconds(60*10));
    }
}
