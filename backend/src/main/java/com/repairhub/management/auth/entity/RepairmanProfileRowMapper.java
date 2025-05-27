package com.repairhub.management.auth.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.repairhub.management.repair.enums.FaultType;

public class RepairmanProfileRowMapper implements RowMapper<RepairmanProfile> {

    @Override
    public RepairmanProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
        return RepairmanProfile.builder()
                .userId(rs.getLong("user_id"))
                .specialty(FaultType.valueOf(rs.getString("specialty")))
                .hourlyMoneyRate(rs.getBigDecimal("hourly_money_rate"))
                .build();
    }
    
}
