package com.example.boardapi.jwt;

import com.example.boardapi.enums.TokenType;
import com.example.boardapi.exception.CustomException;
import com.example.boardapi.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JWTUtil {

    private SecretKey accessKey;
    private SecretKey refreshKey;

    //HS256 : 대칭키 알고리즘,서명 알고리즘
    public JWTUtil(@Value("${jwt.secret}") String access, @Value("${jwt.refresh.secret}") String refresh) {
        this.accessKey = new SecretKeySpec(access.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.refreshKey = new SecretKeySpec(refresh.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    //AccessToken 만료 체크  ||  refreshToken 만료 체크
    public Boolean isTokenExpired(String token, String tokenType) {
        if (!StringUtils.hasText(token)) {
            log.error("토큰이 없습니다.");
            return false;
        }
        // 토큰 타입에 따라서 토큰을 검증할 비밀 키를 정함
        SecretKey currentKey = TokenType.ACCESS_TOKEN.getTokenType().equals(tokenType) ? accessKey : refreshKey;

        Claims claims = parseClaims(token, currentKey);       // 주어진 토큰을 비밀키로 검증한 후 클레임 정보를 반환.

        log.info("토큰 타입 : {}", tokenType);
        log.info("토큰 만료 : {}", claims.getExpiration());

        return claims.getExpiration().before(new Date());   //만료 시간이 현재 시간보다 이전인지 확인.
    }

    // JWT(존왓탱) AccessToken 생성 --> secure cookie 에 저장할 예정
    public String createAccess(String username, String role, int expiredTime) {
        return Jwts.builder()
                .claim("username", username) // 클레임 설정
                .claim("role", role)         //     "
                .issuedAt(new Date(System.currentTimeMillis()))                 // 토큰의 생성 시간
                .expiration(new Date(System.currentTimeMillis() + expiredTime))   //  "    만료 시간
                .signWith(accessKey)    // 암호화 키
                .compact(); // 토큰을 문자열로 직렬화(base64 encoding) / "콤팍또"
    }

    // JWT(존왓탱) RefreshToken 생성 --> DB에 저장할 예정 / 위 메서드 주석과 일치함(참고).
    public String createRefresh(int expiredTime) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredTime))
                .signWith(refreshKey)
                .compact();
    }

    // 주어진 토큰을 비밀키로 검증한 후 클레임 정보를 반환 해주는 메서드
    private Claims parseClaims(String token, SecretKey key) {
        try {
            return Jwts.parser()
                    .verifyWith(key)    // 비밀 키로 서명 검증
                    .build()
                    .parseSignedClaims(token).getPayload();  // 서명된 클레임 파싱
        } catch (ExpiredJwtException e) {
            return e.getClaims();  // 만료된 토큰이라면 클레임을 반환
        } catch (MalformedJwtException e) {
            throw new CustomException(ErrorCode.TOKEN_FORMAT_ERROR);  // 잘못된 형식의 토큰이면 예외 발생
        } catch (SecurityException e) {
            throw new CustomException(ErrorCode.TOKEN_NOT_VALID);  // 보안 오류가 발생하면 예외 발생
        }
    }
}