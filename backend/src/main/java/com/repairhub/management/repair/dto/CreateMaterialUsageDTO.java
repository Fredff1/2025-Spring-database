package com.repairhub.management.repair.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateMaterialUsageDTO {
    private Long orderId;
    private String materialName;

    @Min(1)
    private Integer quantity;
    @Min(0)
    private BigDecimal unitPrice;
}
