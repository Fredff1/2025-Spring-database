package com.repairhub.management.repair.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class RepairFeedbackRowMapper implements RowMapper<RepairFeedback> {

    @Override
    public RepairFeedback mapRow(ResultSet rs, int rowNum) throws SQLException {
        return RepairFeedback.builder()
                .feedbackId(rs.getLong("feedback_id"))
                .orderId(rs.getLong("order_id"))
                .userId(rs.getLong("user_id"))
                .repairmanId(rs.getLong("repairman_id"))
                .rating(rs.getInt("rating"))
                .description(rs.getString("description"))
                .feedbackTime(rs.getTimestamp("feedback_time").toLocalDateTime())
                .build();
    }
    
}
