package com.repairhub.management.repairman.dto;

import com.repairhub.management.repair.enums.FaultType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepairmanProfileUpdateDTO {
    private String username;
    private String phone;
    private String email;
    private FaultType specialty;
}

