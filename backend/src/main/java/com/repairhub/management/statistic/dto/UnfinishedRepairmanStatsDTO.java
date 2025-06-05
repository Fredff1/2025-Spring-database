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
public class UnfinishedRepairmanStatsDTO {
    private String repairmanName;
    private FaultType specialty;
    private Long count;

    public static RowMapper<UnfinishedRepairmanStatsDTO> getRowMapper(){
        return new RowMapper<UnfinishedRepairmanStatsDTO>() {
            @Override
            public UnfinishedRepairmanStatsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return UnfinishedRepairmanStatsDTO.builder()
                .count(rs.getLong("count"))
                .specialty(FaultType.valueOf(rs.getString("specialty")))
                .repairmanName(rs.getString("repairman_name"))
                .build();
            }
        };
    }
}
