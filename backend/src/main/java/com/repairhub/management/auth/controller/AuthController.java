package com.repairhub.management.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.repairhub.management.auth.dto.LoginRequestDTO;
import com.repairhub.management.auth.dto.LoginResponseDTO;
import com.repairhub.management.auth.dto.LogoutRequestDTO;
import com.repairhub.management.auth.dto.LogoutResponseDTO;
import com.repairhub.management.auth.dto.RegisterRequestDTO;
import com.repairhub.management.auth.dto.RegisterResponseDTO;
import com.repairhub.management.auth.entity.User;
import com.repairhub.management.auth.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO requestDTO){
        User user = userService.registerUser(requestDTO);
        var builder = RegisterResponseDTO.builder()
        .success(true);
        var response = builder.build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO requestDTO){
        var responseDto = userService.loginUser(requestDTO);
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDTO> logout(@RequestBody LogoutRequestDTO requestDTO){
        var responseDto = userService.logout(requestDTO.getToken());
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }
}
