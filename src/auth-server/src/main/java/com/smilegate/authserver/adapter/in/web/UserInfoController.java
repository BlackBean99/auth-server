package com.smilegate.authserver.adapter.in.web;


import com.smilegate.authserver.application.port.out.LoadUserPort;
import com.smilegate.authserver.application.port.out.RecordUserPort;
import com.smilegate.authserver.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "WebApplication User 제공 서비스", description = "유저 정보 조회")
public class UserInfoController {
    private final LoadUserPort loadUserPort;
    private final RecordUserPort recordUserPort;

    @Deprecated
    @GetMapping("/api/users/{page}")
    public ResponseEntity<List<User>> findAll(@PathVariable int page) {
        List<User> listUser = loadUserPort.loadAll(page);
        return new ResponseEntity<>(listUser, HttpStatus.OK);
    }

    @Operation(summary = "findUserById", description = "Id로 회원조회")
    @ApiResponses({
            @ApiResponse(responseCode = "Account Object", description = "검색 유저 return")
    })
    @GetMapping("/api/users")
    public ResponseEntity<User> findUserById(Long userId) {
        User user = loadUserPort.findById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "이름으로 회원 조회", description = "동명이인 포함 회원 정보 조회")
    @ApiResponses({
            @ApiResponse(description = "이름으로 회원 조회")
    })
    @GetMapping("/api/users/{userName}")
    public ResponseEntity<List<User>> findUserByUserName(@PathVariable String userName) {
        List<User> userListByUserName = loadUserPort.loadByName(userName);
        return new ResponseEntity<>(userListByUserName, HttpStatus.OK);
    }

    @Operation(summary = "회원정보 수정", description = "로그인된 상태에서, 회원정보 수정")
    @ApiResponses({
            @ApiResponse(description = "수정된 회원 조회 return")
    })
    @PostMapping("/api/users/")
    public ResponseEntity<User> updateUser(HttpServletRequest request, UserUpdateRequestDto userUpdateRequestDto) {
        return new ResponseEntity<>(recordUserPort.updateUser(userUpdateRequestDto), HttpStatus.OK);
    }

}