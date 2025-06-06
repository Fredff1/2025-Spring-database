package com.repairhub.management.repairman.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.repairman.entity.RepairmanProfile;
import com.repairhub.management.statistic.dto.UnfinishedRepairmanStatsDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepairmanBaseInfoDTO {
    private Long repairmanId;
    private String repairmanName;
    private String repairmanNumber;

    public static RepairmanBaseInfoDTO from(RepairmanProfile profile,User repairman){
        return RepairmanBaseInfoDTO.builder()
        .repairmanId(profile.getUserId())
        .repairmanName(repairman.getUsername())
        .repairmanNumber(profile.getRepairmanNumber())
        .build();
    }

    public static RowMapper<RepairmanBaseInfoDTO> getRowMapper(){
        return new RowMapper<RepairmanBaseInfoDTO>() {
            @Override
            public RepairmanBaseInfoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return RepairmanBaseInfoDTO.builder()
                .repairmanId(rs.getLong("repairman_id"))
                .repairmanName(rs.getString("repairman_name"))
                .repairmanNumber(rs.getString("repairman_number"))
                .build();
            }
        };
    }
}
