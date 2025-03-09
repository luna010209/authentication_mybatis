package com.example.authentication_mybatis.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    private String username;
    private String password;
    private String pwConfirm;
    private String newPw;
    private String email;
    private String name;
}
