package com.example.boardapi.jwt;

import com.example.boardapi.entity.Token;
import com.example.boardapi.enums.TokenType;
import com.example.boardapi.exception.CustomException;
import com.example.boardapi.exception.ErrorCode;
import com.example.boardapi.user.CustomUserDetail;
import com.example.boardapi.util.CommonUtils;
import com.example.boardapi.util.CookieUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.boardapi.util.CookieUtils.ACCESS_TOKEN_COOKIE_NAME;
import static com.example.boardapi.util.CookieUtils.REFRESH_TOKEN_COOKIE_NAME;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JWTFilter 발동");
        log.info("현재 url : {}",request.getRequestURI()); // swagger ===> 현재 url : /v3/api-docs/swagger-config
        String accessCookie = CookieUtils.getCookie(request, ACCESS_TOKEN_COOKIE_NAME).orElse("");   // 'Authorization' 쿠키 값 == accessToken 쿠키 값
        String refreshCookie = CookieUtils.getCookie(request, REFRESH_TOKEN_COOKIE_NAME).orElseThrow(()->
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
            log.info("리프레쉬 토큰 만료");
            throw new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }
        Token token = jwtService.validateAndRenewToken(request,response,accessToken,refreshToken);
        
        // UserDetail 회원 정보 저장
        CustomUserDetail customUserDetail = new CustomUserDetail(token.getMember());
        
        // Spring Security AuthToken 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetail,accessToken,customUserDetail.getAuthorities());
        
        // 해당 인증정보를 SecurityContext 에 저장 => 안하면 인증정보 못씀
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
        log.info("JWTFilter 마무리");
    }

    // 토큰(쿠키) 유효성 검사
    public Boolean isTokenValid(String token){
        return CommonUtils.isEmpty(token) || !token.startsWith("Bearer ");

    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/login") || path.startsWith("/api/member/join");
    }
}
