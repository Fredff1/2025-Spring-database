package com.repairhub.management.repair.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.repairhub.management.repair.enums.FeedbackType;

public class RepairFeedbackRowMapper implements RowMapper<RepairFeedback> {

    @Override
    public RepairFeedback mapRow(ResultSet rs, int rowNum) throws SQLException {
        return RepairFeedback.builder()
                .feedbackId(rs.getLong("feedback_id"))
                .orderId(rs.getLong("order_id"))
                .userId(rs.getLong("user_id"))
                .rating(rs.getInt("rating"))
                .description(rs.getString("description"))
                .feedbackTime(rs.getTimestamp("feedback_time").toLocalDateTime())
                .feedbackType(FeedbackType.valueOf(rs.getString("feed_back_type")))
                .adminResponse(rs.getString("admin_response"))
                .build();
    }
    
}
