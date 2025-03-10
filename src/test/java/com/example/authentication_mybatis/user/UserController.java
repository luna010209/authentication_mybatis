package com.example.authentication_mybatis.user;

import com.example.authentication_mybatis.user.dto.UserDto;
import com.example.authentication_mybatis.user.dto.UserLogin;
import com.example.authentication_mybatis.user.dto.UserRequest;
import com.example.authentication_mybatis.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {
    private final UserService userService;
    @PostMapping
    public UserDto newUser(@RequestBody UserRequest request){
        return userService.newUser(request);
    }

    @GetMapping
    public UserDto userLogin(){
        return userService.userLogin();
    }

    @PutMapping
    public UserDto updateUser(@RequestBody UserRequest request){
        return userService.updateUser(request);
    }

    @PutMapping("change-pw")
    public String updatePw(@RequestBody UserRequest request){
        return userService.updatePw(request);
    }
}
