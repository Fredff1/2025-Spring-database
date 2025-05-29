package com.repairhub.management.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.user.dto.UserStatusDTO;
import com.repairhub.management.user.service.UserInfoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserInfoService userInfoService;

    public UserController(
        UserInfoService userInfoService
    ) {
        this.userInfoService = userInfoService;
    }
    
    @GetMapping("/status")
    public ResponseEntity<UserStatusDTO> getStatus(
        @AuthenticationPrincipal User user
    ) {
        var dto = userInfoService.getStatus(user);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
    
}
