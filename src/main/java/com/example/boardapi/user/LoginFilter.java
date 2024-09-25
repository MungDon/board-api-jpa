package com.example.boardapi.user;

import com.example.boardapi.entity.Token;
import com.example.boardapi.exception.CustomException;
import com.example.boardapi.exception.ErrorCode;
import com.example.boardapi.jwt.JWTService;
import com.example.boardapi.util.CommonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> requestMap = objectMapper.readValue(request.getInputStream(), Map.class);

            String email = requestMap.get("email");
            String password = requestMap.get("password");

            log.info("email : {}", email);
            log.info("password : {}", password);

            //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

            //token에 담은 검증을 위한 AuthenticationManager로 전달
            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //로그인 성공시 실행하는 메소드
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        CustomUserDetail principal = (CustomUserDetail) authentication.getPrincipal();
        if (CommonUtils.isEmpty(principal)) {
            throw new CustomException(ErrorCode.USER_AUTHENTICATION_MISSING);
        }

        Token token = jwtService.getOrCreateToken(request, response, principal);
        if (CommonUtils.isEmpty(token)) {
            throw new CustomException(ErrorCode.TOKEN_NOT_VALID);
        }
        log.info("오는척하지마");
        response.sendRedirect(UriComponentsBuilder.fromUriString("/board")
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString());
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.info("오는척하지마2");
        // JSON 응답 객체 생성
        Map<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("message", "로그인 실패");

        // ResponseEntity를 사용하여 JSON 응답 보내기
        ResponseEntity<Map<String, String>> responseEntity = ResponseEntity
                .status(HttpStatus.UNAUTHORIZED) // 401 Unauthorized
                .body(jsonResponse); // JSON 객체

        // JSON 데이터 응답으로 전송
        response.setStatus(responseEntity.getStatusCodeValue());
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity.getBody()));
    }

}
