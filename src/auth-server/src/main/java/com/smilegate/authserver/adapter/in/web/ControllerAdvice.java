package com.smilegate.authserver.adapter.in.web;

import jakarta.ws.rs.BadRequestException;
import jdk.jshell.spi.ExecutionControl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalExHandle(IllegalArgumentException e) {
        log.error(new Date().getTime() + "  [부적절한 사용자 요청] : ", e.getMessage());
        return new ResponseEntity("사용자의 잘못된 요청입니다.", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<String> userExHandle(ExecutionControl.UserException e) {
        log.error(new Date().getTime() + "  [Exception] : ", e.getMessage());
        return new ResponseEntity<>("Exception", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> signUpExHandle(Exception e) {
        log.error(new Date().getTime() + "  [BadRequestException] : ", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>("SIGH_UP", HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ResponseEntity<String> exHandle(Exception e) {
        log.error(new Date().getTime() + "  [INTERNAL_SERVER_ERROR] : ", e.getMessage());
        return new ResponseEntity<>("INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
