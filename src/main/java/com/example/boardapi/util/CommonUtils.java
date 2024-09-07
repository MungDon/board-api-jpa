package com.example.boardapi.util;

import com.example.boardapi.exception.CustomException;
import com.example.boardapi.exception.ErrorCode;
import com.example.boardapi.response.ResComResult;

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
    public static ResComResult successResponse(Long sid,String message, ErrorCode errorCode){
        if(isEmpty(sid)){
            throw new CustomException(errorCode);
        }
        return ResComResult.builder().success(true).message(message).statusCode(200).build();
    }

}
