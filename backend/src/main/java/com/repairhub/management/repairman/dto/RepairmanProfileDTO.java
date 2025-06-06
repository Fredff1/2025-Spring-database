package com.repairhub.management.repairman.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.repairhub.management.auth.domain.enums.UserStatus;
import com.repairhub.management.repair.enums.FaultType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepairmanProfileDTO {
    private Long id;
    private String username;
    private String phone;
    private String email;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private UserStatus userStatus;
    private FaultType specialty;
    private BigDecimal hourlyMoneyRate;
    private String repairmanNumber;
}
