package com.example.boardapi.config;


import com.example.boardapi.exception.CustomException;
import com.example.boardapi.exception.ErrorCode;
import com.example.boardapi.jwt.JWTFilter;
import com.example.boardapi.jwt.JWTService;
import com.example.boardapi.jwt.JWTUtil;
import com.example.boardapi.repository.MemberRepository;
import com.example.boardapi.user.LoginFilter;
import com.example.boardapi.util.CookieUtils;
import io.jsonwebtoken.Header;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import static com.example.boardapi.util.CookieUtils.ACCESS_TOKEN_COOKIE_NAME;
import static com.example.boardapi.util.CookieUtils.REFRESH_TOKEN_COOKIE_NAME;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTUtil jwtUtil;
    private final JWTService jwtService;
    private final MemberRepository memberRepository;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                "/h2-console/**",               // H2 콘솔 접근 허용
                "/favicon.ico",                 // 파비콘 접근 허용
                "/error",                       // 에러 페이지 접근 허용
                "/swagger-ui/**",               // Swagger UI 접근 허용
                "/swagger-resources/**",        // Swagger 리소스 접근 허용
                "/v3/api-docs/**");             // OpenAPI 문서 접근 허용 // swagger ===> 현재 url : /v3/api-docs/swagger-config
    }

    // Security Filter Chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.addAllowedOriginPattern("http://localhost:3000");
                        config.addAllowedOriginPattern("https://localhost:3000");
                        config.addAllowedHeader("*");
                        config.addAllowedMethod("*");
                        config.setAllowCredentials(true);
                        return config;
                    }
                }))
                .headers(headerConfigurer -> headerConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) //For H2 DB
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout((logoutConfig)->
                    logoutConfig
                        .logoutUrl("/api/member/logout")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .logoutSuccessHandler((request, response, authentication) ->{
                            String refreshCookie = CookieUtils.getCookie(request,REFRESH_TOKEN_COOKIE_NAME).orElseThrow(()->
                                    new CustomException(ErrorCode.REFRESH_TOKEN_NOT_VALID));
                            jwtService.removeToken(refreshCookie);
                            CookieUtils.deleteCookie(request, response, ACCESS_TOKEN_COOKIE_NAME);
                            CookieUtils.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setCharacterEncoding("UTF-8");
                            response.setStatus(HttpStatus.OK.value());
                            response.getWriter().write("{\"success\": true, \"message\": \"로그아웃 되었습니다\"}");
                        }))
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(antMatcher("/api/board/**")).permitAll()
                        .requestMatchers(antMatcher("/error, /home")).permitAll()
                        .requestMatchers(antMatcher("/api/member/login")).permitAll()
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAt(new LoginFilter(jwtService,authenticationManager(),memberRepository), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTFilter(jwtUtil,jwtService),LoginFilter.class);
        return http.build();
    }
}
