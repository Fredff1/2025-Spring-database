package com.repairhub.management.order.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.repairhub.management.order.enums.OrderStatus;
import com.repairhub.management.repair.enums.FaultType;

public class RepairOrderRowMapper implements RowMapper<RepairOrder> {

    @Override
    public RepairOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
        return RepairOrder.builder()
                .orderId(rs.getLong("order_id"))
                .userId(rs.getLong("user_id"))
                .vehicleId(rs.getLong("vehicle_id"))
                .submitTime(rs.getTimestamp("submit_time").toLocalDateTime())
                .updateTime(rs.getTimestamp("update_time").toLocalDateTime())
                .status(OrderStatus.valueOf(rs.getString("status")))
                .description(rs.getString("description"))
                .faultType(FaultType.valueOf(rs.getString("fault_type")))
                .totalFee(rs.getBigDecimal("total_fee"))
                .isPaid(rs.getBoolean("is_paid"))
                .build();
    }
    
}
