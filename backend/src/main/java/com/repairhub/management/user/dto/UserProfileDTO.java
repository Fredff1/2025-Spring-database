package com.repairhub.management.user.dto;

import java.time.LocalDateTime;

import com.repairhub.management.auth.domain.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileDTO {
    private Long id;
    private String username;
    private String phone;
    private String email;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private UserStatus userStatus;
}
