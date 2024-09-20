package com.example.boardapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        // 보안 스킴 명
        String accessScheme = "accessToken";
        String refreshScheme = "refreshToken";


        // API 요청시 필요한 보안 요구 사항 정의
        // SecurityRequirement : API 의 특정 엔드포인트에 대해 요구되는 보안 스킴을 지정하는 객체이다.
        //                       -> API 를 호출 할 때 클라이언트가 어떤 인증 정보를 제공해야하는지 정의
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(accessScheme)
                .addList(refreshScheme);

        // 보안 스킴 등록
        Components components = new Components()
                .addSecuritySchemes(accessScheme, new SecurityScheme() // accessScheme
                        .name(accessScheme) // 스킴 이름 설정
                        .type(SecurityScheme.Type.APIKEY) // API 키 타입으로 설정  주로 요청 헤더나 쿼리 파라미터에 API 키를 포함시켜 인증함 / 다른 방법에는 HTTP 타입도 있는듯
                        .in(SecurityScheme.In.COOKIE) // 쿠키에서 가져오는 것으로 설정
                        .scheme("bearer") // Bearer 방식으로 인증
                        .bearerFormat("JWT") // Bearer 토큰 형식은 JWT
                        .name("Authorization"))// HTTP 쿠키 이름 설정
                .addSecuritySchemes(refreshScheme, new SecurityScheme()// refreshScheme
                        .name(refreshScheme)
                        .in(SecurityScheme.In.COOKIE)
                        .scheme("bearer")
                        .bearerFormat("Refresh Token")// Bearer 토큰 형식은 Refresh Token
                        .name("X-Refresh-Token"));

        return new OpenAPI()
                .components(components)
                .info(apiInfo())
                .addSecurityItem(securityRequirement);
    }

    private Info apiInfo() {
        return new Info()
                .title("BoardApi")
                .description("욤마스윀")
                .version("1.0.0");
    }
}
