package com.example.authentication_mybatis.user.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserLogin implements UserDetails, OAuth2User {
    private Long id;
    private String username;
    private String hashedPw;
    private String email;
    private String name;
    private String authority;

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> map= new HashMap<>();
        map.put("username", username);
        map.put("email", email);
        map.put("name", name);
        return map;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String[] authorityStrings= this.authority.split(",");
        return Arrays.stream(authorityStrings).map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public String getPassword() {
        return this.hashedPw;
    }
}
