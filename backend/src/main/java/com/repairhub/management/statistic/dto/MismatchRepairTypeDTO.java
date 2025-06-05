package com.repairhub.management.statistic.dto;

import java.time.LocalDateTime;

import com.repairhub.management.repair.enums.FaultType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MismatchRepairTypeDTO {
    private Long repairRecordId;            

    private Long orderId;                   

    private String repairmanName;           

    private FaultType repairmanSpecialty;      

    private FaultType orderFaultType;          

    private LocalDateTime completionTime;   

}
