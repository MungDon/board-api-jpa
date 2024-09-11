package com.example.boardapi.util;

import com.example.boardapi.exception.CustomException;
import com.example.boardapi.exception.ErrorCode;
import com.example.boardapi.response.ResComResult;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Map;

public class CommonUtils {

    public static boolean isEmpty(Object data) {
        if (data == null) {
            return true;
        }
        if (data instanceof String && "".equals(data)) {
            return true;
        }
        if (data instanceof Collection && ((Collection<?>) data).isEmpty()) {
            return true;
        }
        if (data instanceof Map && ((Map<?, ?>) data).isEmpty()) {
            return true;
        }
        if (data instanceof Object[] && ((Object[]) data).length == 0) {
            return true;
        }
        return false;
    }
    /*반환할 객체 없는 응답 객체 생성*/
    public static ResComResult successResponseNoData(Long sid,String message, ErrorCode errorCode){
        if(isEmpty(sid)){
            throw new CustomException(errorCode);
        }
        return ResComResult.builder().success(true).message(message).statusCode(200).build();
    }

    /*반환할 객체 있는 응답 객체 생성*/
    public static ResComResult successResponseHasData(Long sid,String message, ErrorCode errorCode,Object data){
        if(isEmpty(sid)){
            throw new CustomException(errorCode);
        }
        return ResComResult.builder().success(true).message(message).statusCode(200).data(data).build();
    }
    
    /* 토큰 유효 시간을 LocalDateTime 으로 변환*/
    public static LocalDateTime calculateExpiryFromNow(int expiredTime){
        // 현재 시간의 Instant 객체 생성
        Instant currentInstant = Instant.now();

        // 현재 Instant에 밀리초를 추가하여 새로운 Instant 객체 생성
        Instant expiryInstant = currentInstant.plus(expiredTime, ChronoUnit.MILLIS);

        // Instant를 LocalDateTime으로 변환
        return LocalDateTime.ofInstant(expiryInstant, ZoneId.systemDefault());
    }

    /*조건문에 따라 예외 발생*/
    public static void throwCustomExceptionIf(boolean conditionalStatement, ErrorCode errorCode) {
        if(conditionalStatement) {
            throw new CustomException(errorCode);
        }
    }
}
