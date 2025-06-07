package com.repairhub.management.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.repairhub.management.auth.domain.enums.UserStatus;
import com.repairhub.management.auth.dto.LoginRequestDTO;
import com.repairhub.management.auth.dto.LoginResponseDTO;
import com.repairhub.management.auth.dto.LogoutResponseDTO;
import com.repairhub.management.auth.dto.RegisterRequestDTO;
import com.repairhub.management.auth.entity.User;
import com.repairhub.management.auth.exception.DuplicatedUserException;
import com.repairhub.management.auth.exception.InvalidPasswordException;
import com.repairhub.management.auth.exception.UserNotFoundException;
import com.repairhub.management.auth.repository.UserRepository;
import com.repairhub.management.utils.JwtUtil;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public UserService(
        UserRepository userRepository,
        JwtUtil jwtUtil,
        PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager){
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User registerUser(RegisterRequestDTO createUserDTO){
        String username = createUserDTO.getUsername();
        if(userRepository.findByUsername(username).isPresent()){
            throw new DuplicatedUserException("User already exists");
        }
        if(!createUserDTO.getPassword().equals(createUserDTO.getConfirmPassword())){
            throw new InvalidPasswordException("Passwords do not match");
        }
        User user = User.builder()
        .username(createUserDTO.getUsername())
        .password(passwordEncoder.encode(createUserDTO.getPassword()))
        .email(createUserDTO.getEmail())
        .status(UserStatus.ACTIVE)
        .role(createUserDTO.getRole())
        .phone(createUserDTO.getPhone())
        .build();
        userRepository.insert(user);
        return user;
    }

    public LoginResponseDTO loginUser(LoginRequestDTO loginDTO){
        User user = userRepository.findByUsername(loginDTO.getUsername())
        .orElseThrow(() -> new UserNotFoundException("User not found"));
         UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword());
        var authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtil.generateToken(authentication.getName());
        LoginResponseDTO responseDTO = LoginResponseDTO.builder()
        .token(token)
        .role(user.getRole())
        .build();
        return responseDTO;
    }

    public LogoutResponseDTO logout(String token){
        jwtUtil.addToBlacklist(token);
        return LogoutResponseDTO.builder().success(true).build();
    }

    public int deleteUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
        return userRepository.deleteById(user.getUserId());
    }

    public Long findUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getUserId();
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
