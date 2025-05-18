package com.repairhub.management.auth.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long    userId;
    private String  username;
    private String  password;
    private String  email;
    private String  phone;
    private String  status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
