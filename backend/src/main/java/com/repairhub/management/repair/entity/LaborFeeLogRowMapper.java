package com.repairhub.management.repair.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;

import org.springframework.jdbc.core.RowMapper;

public class LaborFeeLogRowMapper implements RowMapper<LaborFeeLog> {

    @Override
    public LaborFeeLog mapRow(ResultSet rs, int rowNum) throws SQLException {
        return LaborFeeLog.builder()
                .LaborFeeLogId(rs.getLong("labor_fee_log_id"))
                .repairmanId(rs.getLong("repairman_id"))
                .month(Month.valueOf(rs.getString("month").toUpperCase()))
                .totalHours(rs.getBigDecimal("total_hours"))
                .totalIncome(rs.getBigDecimal("total_income"))
                .settleTime(rs.getTimestamp("settle_time").toLocalDateTime())
                .build();
    }
    
}
