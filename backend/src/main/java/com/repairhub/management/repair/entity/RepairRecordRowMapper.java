package com.repairhub.management.repair.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.repairhub.management.order.enums.OrderStatus;

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
            .actualWorkHour(rs.getBigDecimal("actual_work_hours"))
            .orderStatus(OrderStatus.valueOf(rs.getString("order_status")))
            .build();
    }
    
}
