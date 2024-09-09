package com.example.boardapi.jwt;

import com.example.boardapi.entity.Member;
import com.example.boardapi.entity.Token;
import com.example.boardapi.enums.TokenExpire;
import com.example.boardapi.exception.CustomException;
import com.example.boardapi.exception.ErrorCode;
import com.example.boardapi.repository.JWTRepository;
import com.example.boardapi.util.CommonUtils;
import com.example.boardapi.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JWTService {

    private final JWTUtil jwtUtil;
    private final JWTRepository jwtRepository;

    @Transactional
    public Token validateAndRenewToken(HttpServletRequest req, HttpServletResponse res, String accessToken, String refreshToken){
        // 현재 쿠키에 등록된 RefreshToken 이 DB에 저장된 RefreshToken 과 일치하는지 검사
        Token token = jwtRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_VALID));

        if(CommonUtils.isEmpty(accessToken)){   // accessToken 이 만료되었다면
            Token renewToken = renewToken(req,res,token.getMember()); // 새로 발급 후
            jwtRepository.delete(token);        //  이전 RefreshToken 은 폐기 JWT Token Rotation 방식
            return renewToken;
        }
        // 만료되지 않았다면 현재 토큰 반환
        return token;
    }

    @Transactional
    public Token renewToken(HttpServletRequest req, HttpServletResponse res, Member member){
        // accessToken 과 refreshToken 생성
        String accessToken = jwtUtil.createAccess(member.getEmail(),member.getRole(), TokenExpire.ACCESS_TOKEN.getExpiredTime());
        String refreshToken = jwtUtil.createRefresh(TokenExpire.REFRESH_TOKEN.getExpiredTime());

        // DB에 저장할 RefreshToken 만료 시간
        LocalDateTime refreshExpireTime = CommonUtils.calculateExpiryFromNow(TokenExpire.REFRESH_TOKEN.getExpiredTime());

        Token renewToken = Token.builder()
                .refreshToken(refreshToken)
                .member(member)
                .refreshExpireTime(refreshExpireTime)
                .build();

        jwtRepository.save(renewToken);
        CookieUtils.reissueTokenCookie(req,res,accessToken,refreshToken);
        return renewToken;
    }
}
