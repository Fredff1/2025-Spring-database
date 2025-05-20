package com.repairhub.management.auth.dto;

import java.time.LocalDateTime;

import com.repairhub.management.auth.domain.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDTO {
    private Long id;
    private String username;
    private String name;
    private String phone;
    private String email;
    private UserRole role;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
