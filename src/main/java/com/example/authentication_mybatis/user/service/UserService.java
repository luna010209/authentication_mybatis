package com.example.authentication_mybatis.user.service;

import com.example.authentication_mybatis.emailVerify.dto.EmailDto;
import com.example.authentication_mybatis.emailVerify.mapper.EmailMapper;
import com.example.authentication_mybatis.exception.CustomException;
import com.example.authentication_mybatis.user.dto.UserDto;
import com.example.authentication_mybatis.user.dto.UserLogin;
import com.example.authentication_mybatis.user.dto.UserRequest;
import com.example.authentication_mybatis.user.mapper.UserMapper;
import com.example.authentication_mybatis.user.userLogin.UserComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;
    private final EmailMapper emailMapper;
    private final UserComponent userComponent;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserLogin userLogin = userMapper.findByUsername(username);
        if (userLogin==null)
            throw new CustomException(HttpStatus.NOT_FOUND, "Username does not exist!!");
        return userLogin;
    }

    public UserDto newUser(UserRequest request){
        if (userMapper.existsByUsername(request.getUsername()))
            throw new CustomException(HttpStatus.BAD_REQUEST, "Username already exists!");
        else if (userMapper.existsByEmail(request.getEmail()))
            throw new CustomException(HttpStatus.BAD_REQUEST, "Email already exists!");
        // Check email for verification
        EmailDto email = emailMapper.findByEmail(request.getEmail());
        if (email==null || !email.isSuccess())
            throw new CustomException(HttpStatus.BAD_REQUEST, "Your email is not verified!");
        else if (!request.getPassword().equals(request.getPwConfirm()))
            throw new CustomException(HttpStatus.CONFLICT, "Password and password confirm do not match!");
        UserDto dto = UserDto.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .name(request.getName())
                .avatar("/static/visual/user.png")
                .build();
        UserLogin userLogin = UserLogin.builder()
                .username(request.getUsername())
                .hashedPw(encoder.encode(request.getPassword()))
                .email(request.getEmail())
                .name(request.getName())
                .authority("ROLE_USER")
                .build();
        userMapper.createUser(dto);
        userMapper.userInfo(userLogin);
        return dto;
    }

    public UserDto userLogin(){
        return userComponent.userLogin();
    }

    public UserDto updateUser(UserRequest request){
        UserDto user = userComponent.userLogin();
        UserLogin userLogin = userMapper.findByUsername(user.getUsername());
        if (!request.getUsername().isEmpty()){
            user.setUsername(request.getUsername());
            userLogin.setUsername(request.getUsername());
        }

        if (!request.getEmail().isEmpty()){
            user.setEmail(request.getEmail());
            userLogin.setEmail(request.getEmail());
        }

        if (!request.getName().isEmpty()){
            user.setName(request.getName());
            userLogin.setName(request.getName());
        }

        userMapper.updateUser(user);
        userMapper.updateLogin(userLogin);
        return user;
    }

    public String updatePw(UserRequest request){
        UserDto user = userComponent.userLogin();
        UserLogin userLogin = userMapper.findByUsername(user.getUsername());
        if (!encoder.matches(request.getPassword(), userLogin.getPassword()))
            throw new CustomException(HttpStatus.CONFLICT, "Wrong password!");
        else if (!request.getNewPw().equals(request.getPwConfirm()))
            throw new CustomException(HttpStatus.BAD_REQUEST, "Password and password confirm do not match!");
        userLogin.setHashedPw(encoder.encode(request.getNewPw()));
        userMapper.updateLogin(userLogin);
        return "Change password successfully";
    }

    public String updateAvatar(MultipartFile file){
        UserDto user = userComponent.userLogin();
        String directory = "/profile/"+ user.getId()+"/";
        try{
            Files.createDirectories(Path.of(directory));
        } catch (IOException e){
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Fail to create directory");
        }
        String filename = file.getOriginalFilename();
        String[] eles = filename.split("\\.");
        String extension = eles[eles.length-1];
        String path = directory+"avatar."+extension;
        try{
            file.transferTo(Path.of(path));
        } catch(IOException e){
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Fail to upload file");
        }
        String url = "/static/"+user.getId()+"/avatar."+extension;
        user.setAvatar(url);
        userMapper.updateUser(user);
        return "Change avatar successfully";
    }
}
