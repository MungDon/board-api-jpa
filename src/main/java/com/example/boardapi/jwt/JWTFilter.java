package com.example.boardapi.jwt;

import com.example.boardapi.enums.TokenType;
import com.example.boardapi.exception.CustomException;
import com.example.boardapi.exception.ErrorCode;
import com.example.boardapi.util.CommonUtils;
import com.example.boardapi.util.CookieUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JWTFilter 발동");

        String accessCookie = CookieUtils.getCookie(request, "Authorization").orElse("");   // 'Authorization' 쿠키 값 == accessToken 쿠키 값
        String refreshCookie = CookieUtils.getCookie(request, "X-Refresh-Token").orElseThrow(()->
                new CustomException(ErrorCode.REFRESH_TOKEN_NOT_VALID)); // 'X-Refresh-Token' 쿠키 값, 없으면 예외 발생

        // 가져온 토큰(쿠키)의 유효성검사
        if(isTokenValid(refreshCookie)){
            throw new CustomException(ErrorCode.REFRESH_TOKEN_NOT_VALID);
        }

        // 각 쿠키의 Bearer 공백뒤에 실제 토큰 값을 가져옴
        // Bearer aasaswudgoqwd~~~
        String accessToken = isTokenValid(accessCookie) ? "" : accessCookie.split(" ")[1] ;
        log.info("accessToken : {}",accessToken);
        String refreshToken  = refreshCookie.split(" ")[1];
        log.info("refreshToken : {}",refreshToken);

        if(jwtUtil.isTokenExpired(refreshToken, TokenType.REFRESH_TOKEN.getTokenType())){
            throw new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }
        // TODO -- (2024.09.08)리프레쉬 토큰이 있는상황 = DB 에 있는 해당 사용자의 refresh Token 값과 비교후 AccessToken 재발급 로직 추가 예정
    }

    // 토큰(쿠키) 유효성 검사
    public Boolean isTokenValid(String token){
        return CommonUtils.isEmpty(token) || !token.startsWith("Bearer ");

    }
}
