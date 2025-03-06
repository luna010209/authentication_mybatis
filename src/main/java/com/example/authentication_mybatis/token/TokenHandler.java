package com.example.authentication_mybatis.token;

import com.example.authentication_mybatis.user.dto.UserLogin;
import com.example.authentication_mybatis.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@RequiredArgsConstructor
@Slf4j
public class TokenHandler extends OncePerRequestFilter {
    private final TokenUtils tokenUtils;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header==null){
            filterChain.doFilter(request, response);
            return;
        }

        String[] splitHeader = header.split(" ");
        if (!(splitHeader.length==2 && splitHeader[0].equals("Bearer"))){
            filterChain.doFilter(request, response);
            return;
        }

        String token = splitHeader[1];
        String username = tokenUtils.claims(token).getSubject();
        log.info(username);
        UserLogin userLogin = (UserLogin) userService.loadUserByUsername(username);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userLogin,
                userLogin.getPassword(),
                userLogin.getAuthorities()
        );
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        filterChain.doFilter(request, response);
    }
}
