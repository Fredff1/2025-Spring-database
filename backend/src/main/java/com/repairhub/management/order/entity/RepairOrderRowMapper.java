package com.repairhub.management.order.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class RepairOrderRowMapper implements RowMapper<RepairOrder> {

    @Override
    public RepairOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
        return RepairOrder.builder()
                .orderId(rs.getLong("order_id"))
                .userId(rs.getLong("user_id"))
                .vehicleId(rs.getLong("vehicle_id"))
                .submitTime(rs.getTimestamp("submit_time").toLocalDateTime())
                .status(rs.getString("status"))
                .description(rs.getString("description"))
                .build();
    }
    
}
