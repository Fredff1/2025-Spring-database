package com.repairhub.management.auth.dto;

import com.repairhub.management.auth.domain.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserStatusDTO {
    private UserStatus status;
}
