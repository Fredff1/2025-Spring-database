package com.repairhub.management.order.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.repairhub.management.order.enums.AssignmentStatus;

public class OrderAssignmentRowMapper implements RowMapper<OrderAssignment> {

    @Override
    public OrderAssignment mapRow(ResultSet rs, int rowNum) throws SQLException {
        return OrderAssignment.builder()
                .assignmentId(rs.getLong("assignment_id"))
                .orderId(rs.getLong("order_id"))
                .repairmanId(rs.getLong("repairman_id"))
                .status(AssignmentStatus.valueOf(rs.getString("assignment_status")))
                .assignmentTime(rs.getTimestamp("assignment_time").toLocalDateTime())
                .actualWorkHour(rs.getBigDecimal("actual_work_hours"))
                .build();
    }
    
}
