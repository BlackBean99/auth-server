package com.smilegate.authserver.application.service;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class LoginFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof BadCredentialsException) {
            request.setAttribute("LoginFailMessage", "아이디 또는 비밀번호가 일치하지 않습니다");
        } else if (exception instanceof DisabledException) {
            request.setAttribute("LoginFailMessage", "인증받지 못한 계정입니다.");
        } else if (exception instanceof LockedException) {
            request.setAttribute("LoginFailMessage", "현재 잠긴 계정입니다");
        } else if (exception instanceof AccountExpiredException) {
            request.setAttribute("LoginFailMessage", "이미 만료된 계정입니다.");
        } else if (exception instanceof CredentialsExpiredException) {
            request.setAttribute("LoginFailMessage", "비밀번호가 만료된 계정입니다.");
        } else request.setAttribute("LoginFailMessage", "계정을 찾을 수 없습니다.");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/login.html");
        dispatcher.forward(request, response);
    }
}
