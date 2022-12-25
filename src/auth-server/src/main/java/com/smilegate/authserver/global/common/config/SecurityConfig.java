package com.smilegate.authserver.global.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smilegate.authserver.application.service.AccessFailHandler;
import com.smilegate.authserver.application.service.LoginFailHandler;
import com.smilegate.authserver.application.service.LoginSuccessHandler;
import com.smilegate.authserver.global.common.config.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ObjectMapper objectMapper;
    private final AccessFailHandler accessFailHandler;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailHandler loginFailHandler;

    private final String UNAUTHORIZEd_CUSTOM_MESSAGE = "인증받지 못한 유저입니다. 로그인을 재시도해주세요.";


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.securityMatcher("/**")
                .cors().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                특정 URL 차단 및 접근권한 설정
                .and()
                .authorizeRequests()// 시큐리티 처리에 HttpServeltRequest를 사용합니다.
                .requestMatchers("/api/accounts/**").permitAll()
                .requestMatchers("/api/users/**").hasRole("USER")
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)  //JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다
                .exceptionHandling()
                .accessDeniedHandler(accessFailHandler)
                .and()
                .formLogin()
                .loginProcessingUrl("/api/accounts/login/process")
//                .successHandler()
                .failureHandler(loginFailHandler)
                .successHandler(loginSuccessHandler)
                .and()
                .build();
    }

}