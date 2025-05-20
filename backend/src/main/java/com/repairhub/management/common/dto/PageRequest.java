package com.repairhub.management.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageRequest {
    private Integer page = 1;
    private Integer limit = 10;
    
}
