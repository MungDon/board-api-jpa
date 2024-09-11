package com.example.boardapi.user;

import com.example.boardapi.entity.Token;
import com.example.boardapi.exception.CustomException;
import com.example.boardapi.exception.ErrorCode;
import com.example.boardapi.jwt.JWTService;
import com.example.boardapi.util.CommonUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //클라이언트 요청에서 username, password 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        //token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        CustomUserDetail principal = (CustomUserDetail) authentication.getPrincipal();
        if(CommonUtils.isEmpty(principal)){
            throw new CustomException(ErrorCode.USER_AUTHENTICATION_MISSING);
        }

        Token token = jwtService.getOrCreateToken(request,response,principal);
        if(CommonUtils.isEmpty(token)){
            throw new CustomException(ErrorCode.TOKEN_NOT_VALID);
        }
        response.sendRedirect(UriComponentsBuilder.fromUriString("/api/board")
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString());
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.sendRedirect(UriComponentsBuilder.fromUriString("/home")
                .queryParam("error", "로그인 실패")
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString());
    }

}
