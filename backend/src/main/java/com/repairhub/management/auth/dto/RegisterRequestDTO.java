package com.repairhub.management.auth.dto;

import java.util.List;

import com.repairhub.management.auth.domain.enums.UserRole;
import com.repairhub.management.auth.domain.enums.UserStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDTO {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private UserRole role;

    private String phone;

    private UserRole roles;



}
