package com.repairhub.management.repairman.dto;

import java.math.BigDecimal;

import com.repairhub.management.auth.domain.enums.UserRole;
import com.repairhub.management.repair.enums.FaultType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepairmanCreateDTO {
    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String confirmPassword;

    @NotNull
    private UserRole role;

    private String email;

    private String phone;

    private FaultType specialty;

    private BigDecimal hourlyMoneyRate;

    private String repairmanNumber;
}
