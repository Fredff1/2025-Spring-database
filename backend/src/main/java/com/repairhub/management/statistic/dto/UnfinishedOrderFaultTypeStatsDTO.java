package com.repairhub.management.statistic.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.repairhub.management.repair.enums.FaultType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnfinishedOrderFaultTypeStatsDTO {
    private Long count;
    private FaultType faultType;

    public static RowMapper<UnfinishedOrderFaultTypeStatsDTO> getRowMapper(){
        return new RowMapper<UnfinishedOrderFaultTypeStatsDTO>() {
            @Override
            public UnfinishedOrderFaultTypeStatsDTO mapRow(ResultSet rs, int rowNum) throws SQLException{
                return UnfinishedOrderFaultTypeStatsDTO.builder()
                .count(rs.getLong("count"))
                .faultType(FaultType.valueOf(rs.getString("fault_type")))
                .build();
            }
        };
    }
}
