package com.example.authentication_mybatis.emailVerify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class EmailRequest {
    private String email;
    private Integer code;
}
