package com.repairhub.management.statistic.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnfinishedVehicleStatsDTO {
    private String licensePlate; 
    private String brand; 
    private String model; 
    private Long count;

    public static RowMapper<UnfinishedVehicleStatsDTO> getRowMapper(){
        return new RowMapper<UnfinishedVehicleStatsDTO>() {
            @Override
            public UnfinishedVehicleStatsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return UnfinishedVehicleStatsDTO.builder()
                .licensePlate(rs.getString("license_plate"))
                .brand(rs.getString("brand"))
                .model(rs.getString("model"))
                .count(rs.getLong("count"))
                .build();
            }
        };
    }
}
