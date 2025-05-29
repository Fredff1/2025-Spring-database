package com.repairhub.management.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserStatusDTO {
    private Integer vehicleCount;
    private Integer orderCount;
    private Integer historyCount;

}
