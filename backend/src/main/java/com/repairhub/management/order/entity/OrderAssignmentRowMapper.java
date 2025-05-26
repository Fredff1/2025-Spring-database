package com.repairhub.management.order.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class OrderAssignmentRowMapper implements RowMapper<OrderAssignment> {

    @Override
    public OrderAssignment mapRow(ResultSet rs, int rowNum) throws SQLException {
        return OrderAssignment.builder()
                .assignmentId(rs.getLong("assign_id"))
                .orderId(rs.getLong("order_id"))
                .repairmanId(rs.getLong("repairman_id"))
                .accepted(rs.getBoolean("accepted"))
                .assignmentTime(rs.getTimestamp("assigned_time").toLocalDateTime())
                .actualWorkHour(rs.getBigDecimal("actual_work_hour"))
                .build();
    }
    
}
