package com.example.boardapi.config;

import com.example.boardapi.util.AuthUserDataResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthUserDataResolver authUserDataResolver;

    // 커스텀 어노테이션 리졸버 등록 / 이걸안하면 커스텀 어노테이션이 동작을 안함
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authUserDataResolver);
    }
}
