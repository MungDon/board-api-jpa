package com.example.boardapi.util;

import com.example.boardapi.exception.CustomException;
import com.example.boardapi.exception.ErrorCode;
import com.example.boardapi.user.CustomUserDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
public class AuthUserDataResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthUserData.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = (Authentication) webRequest.getUserPrincipal();
        if(CommonUtils.isEmpty(authentication)){
            throw new CustomException(ErrorCode.USER_AUTHENTICATION_MISSING);
        }

        if(authentication.getPrincipal() instanceof CustomUserDetail){
            CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
            return userDetail.member();
        }
        throw new CustomException(ErrorCode.USER_ENTITY_MISSING);
    }
}
