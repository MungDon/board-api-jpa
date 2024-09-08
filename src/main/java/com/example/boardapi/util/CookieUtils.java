package com.example.boardapi.util;

import com.example.boardapi.exception.CustomException;
import com.example.boardapi.exception.ErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;

public class CookieUtils {

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
}
