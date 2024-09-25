package com.example.boardapi.util;

import com.example.boardapi.enums.TokenExpire;
import com.example.boardapi.exception.CustomException;
import com.example.boardapi.exception.ErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Optional;

@Slf4j
public class CookieUtils {

    public static final String ACCESS_TOKEN_COOKIE_NAME = "Authorization";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "X-Refresh-Token";

    public static Optional<String> getCookie(HttpServletRequest request, String cookieName){
        Cookie[] cookies = request.getCookies();        // 모든 쿠키를 가져옴
        if(!CommonUtils.isEmpty(cookies)){              // 가져온 쿠기가 있으면 
            for(Cookie cookie : cookies){                
                if(cookie.getName().equals(cookieName)){// 넘겨받은 쿠키의 이름과 일치하는 것을 찾아
                    return Optional.of(decodeCookie(cookie.getValue()));  // 쿠키의 값을 디코딩 후 반환
                }
            }
        }
        return Optional.empty();
    }

    public static String decodeCookie(String cookieValue){
        try {
            return URLDecoder.decode(cookieValue,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new CustomException(ErrorCode.UTF8_ENCODING_NOT_SUPPORTED);
        }
    }

    public static String tokenEncodeForCookie(String token){
        try {
            return URLEncoder.encode("Bearer " + token, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new CustomException(ErrorCode.UTF8_ENCODING_NOT_SUPPORTED);
        }
    }

    public static void reissueTokenCookie(HttpServletRequest req, HttpServletResponse res, String accessToken, String refreshToken){

        String encodeAccessToken = tokenEncodeForCookie(accessToken);
        String encodeRefreshToken = tokenEncodeForCookie(refreshToken);

        deleteCookie(req,res,ACCESS_TOKEN_COOKIE_NAME);
        deleteCookie(req,res,REFRESH_TOKEN_COOKIE_NAME);

        addCookie(req,res,ACCESS_TOKEN_COOKIE_NAME,encodeAccessToken, TokenExpire.ACCESS_TOKEN.getExpiredTime());
        addCookie(req,res,REFRESH_TOKEN_COOKIE_NAME,encodeRefreshToken, TokenExpire.REFRESH_TOKEN.getExpiredTime());
        log.info("쿠키생성");
    }


    /*응답 헤더에 쿠키를 추가하는 이유
    
    클라이언트에 쿠키를 설정하기 위해:
    웹 서버는 클라이언트의 웹 브라우저에 쿠키를 설정하려면 HTTP 응답의 Set-Cookie 헤더를 사용해야 하는데
    이 헤더는 서버가 클라이언트에게 보내는 응답의 일부로 포함된다.
    클라이언트는 이 응답을 받고 쿠키를 자신의 브라우저에 저장한다.

    브라우저가 쿠키를 저장하도록 지시하기 위해:
    브라우저는 서버가 보내는 Set-Cookie 헤더를 읽고, 쿠키를 저장하고
    브라우저는 같은 도메인에 대한 후속 요청 시, 저장된 쿠키를 자동으로 서버에 포함하여 보낸다.
    쿠키를 서버 측에서 설정하지 않으면,
    클라이언트가 서버에 요청을 보낼 때 쿠키가 포함되지 않으므로 서버가 세션이나 상태 정보를 유지할 수 없다.*/
    
    // 응답 정보에 특정 쿠키를 추가
    public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name, String value, int maxAge) {
        // 요청의 서버 이름을 가져온다.
        String server_name = request.getServerName();
        
        // 기본 도메인 이름을 설정한다. => localhost 일때
        String domain = "localhost";

        // 서버 이름을 점(.) 기준으로 나누어 배열로 저장한다.
        String[] server_names = server_name.split("\\.");
        // 서버 이름의 첫 번째 부분을 로깅한다.
        log.info("서버 네임즈 : {}", server_names[0]);

        // 서버 이름이 "localhost"가 아니라면 도메인을 설정한다. => 도메인이 정해져 있을 때
        if (!(server_names[0].equals("localhost"))) {
            domain = server_names[1] + "." + server_names[2];
        }

        // 서버 이름에 숫자가 포함되어 있으면 도메인을 전체 서버 이름으로 설정한다. => IP 일때
        for (String s : server_names) {
            if (s.matches("[0-9]+"))
                domain = server_name;
        }

        // ResponseCookie 객체를 생성하여 쿠키를 설정한다.
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/") // 쿠키 적용 경로(도메인 기준)
                .sameSite("none") // SameSite 속성을 "none"으로 설정 (브라우저 기본값: Lax ==> CSR 에서 쿠키전송 X)
                .httpOnly(true) // HTTP 통신 이외에는 쿠키에 접근 불가능하도록 설정
                .secure(true) // HTTPS 가 아닌 프로토콜에서는 쿠키를 전송하지 않게 설정
                .maxAge(maxAge) // 쿠키의 최대 보관 기간을 설정
                .domain(domain) // 쿠키가 적용될 서버 루트 도메인을 설정
                .build(); // 쿠키를 빌드하여 생성

        // 응답 헤더에 "Set-Cookie"를 추가하여 클라이언트에 쿠키를 설정한다.
        response.addHeader("Set-Cookie", cookie.toString());
        log.info("쿠키 만들긴함?");
    }

    /* 쿠키 삭제 */
    public static void deleteCookie(HttpServletRequest req, HttpServletResponse res, String cookieName){
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    res.addCookie(cookie);
                }
            }
        }
    }

}
