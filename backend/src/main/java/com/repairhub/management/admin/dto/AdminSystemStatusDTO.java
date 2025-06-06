package com.repairhub.management.admin.dto;

import java.math.BigDecimal;
import java.util.Random;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminSystemStatusDTO {
    private BigDecimal cpu;
    private BigDecimal memory;

    public static AdminSystemStatusDTO toDefault(){
        Random random = new Random();
        return AdminSystemStatusDTO.builder()
        .cpu(BigDecimal.valueOf(random.nextLong(40,80)))
        .memory(BigDecimal.valueOf(random.nextLong(40,80)))
        .build();
    }
}
