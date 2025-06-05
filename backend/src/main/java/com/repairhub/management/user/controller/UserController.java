package com.repairhub.management.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.repairhub.management.auth.dto.LoginResponseDTO;
import com.repairhub.management.auth.entity.User;
import com.repairhub.management.user.dto.UserProfileDTO;
import com.repairhub.management.user.dto.UserProfileUpdateDTO;
import com.repairhub.management.user.dto.UserStatusDTO;
import com.repairhub.management.user.service.UserInfoService;
import com.repairhub.management.utils.JwtUtil;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserInfoService userInfoService;
    private final JwtUtil jwtUtil;

    public UserController(
        UserInfoService userInfoService,
        JwtUtil jwtUtil
    ) {
        this.userInfoService = userInfoService;
        this.jwtUtil = jwtUtil;
    }
    
    @GetMapping("/status")
    public ResponseEntity<UserStatusDTO> getStatus(
        @AuthenticationPrincipal User user
    ) {
        var dto = userInfoService.getStatus(user);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getProfile(
        @AuthenticationPrincipal User user
    ) {
        var dto = userInfoService.getProfile(user);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    @PostMapping("/profile/update")
    public ResponseEntity<Map<String, String>> updateProfile(
        @AuthenticationPrincipal User user,
        @RequestBody UserProfileUpdateDTO profile
    ) {
        var updatedProfile = userInfoService.updateProfile(user, profile);
        String newToken = jwtUtil.generateToken(updatedProfile.getUsername());

        return new ResponseEntity<>(Map.of("token", newToken), HttpStatus.OK);
    }
    
    
}
