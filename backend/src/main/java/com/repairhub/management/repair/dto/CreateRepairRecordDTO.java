package com.repairhub.management.repair.dto;

import java.math.BigDecimal;
import java.util.List;

import com.repairhub.management.order.enums.OrderStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateRepairRecordDTO {
    private Long orderId;
    private String faultDescription;
    private String repairResult;
    private OrderStatus status;
    
    @Min(0)
    private BigDecimal actualWorkHour;

    @Valid
    private List<CreateMaterialUsageDTO> materials;
}
