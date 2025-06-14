package com.repairhub.management.repair.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialUsage {
    private Long materialId;
    private Long submitterId;
    private Long orderId;
    private String materialName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private LocalDateTime createTime;
}
