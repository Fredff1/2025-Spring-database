package com.repairhub.management.repair.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class RepairRecordRowMapper implements RowMapper<RepairRecord> {

    @Override
    public RepairRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        return RepairRecord.builder()
            .recordId(rs.getLong("repair_record_id"))
            .orderId(rs.getLong("order_id"))
            .faultDescription(rs.getString("fault_description"))
            .repairResult(rs.getString("repair_result"))
            .completionTime(rs.getTimestamp("completion_time").toLocalDateTime())
            .repairmanId(rs.getLong("repairman_id"))
            .build();
    }
    
}
