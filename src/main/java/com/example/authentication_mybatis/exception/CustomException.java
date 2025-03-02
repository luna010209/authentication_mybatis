package com.example.authentication_mybatis.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CustomException extends RuntimeException{
    private HttpStatus status;
    private String message;
}
