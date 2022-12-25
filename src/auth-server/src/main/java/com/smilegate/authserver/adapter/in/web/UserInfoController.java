package com.smilegate.authserver.adapter.in.web;


import com.smilegate.authserver.application.port.out.LoadUserPort;
import com.smilegate.authserver.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "WebApplication User 제공 서비스", description = "유저 정보 조회")
public class UserInfoController {
    private final LoadUserPort loadUserPort;

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


}