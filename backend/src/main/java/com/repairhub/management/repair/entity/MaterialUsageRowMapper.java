package com.repairhub.management.repair.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MaterialUsageRowMapper implements RowMapper<MaterialUsage> {

    @Override
    public MaterialUsage mapRow(ResultSet rs, int rowNum) throws SQLException {
        return MaterialUsage.builder()
                .materialId(rs.getLong("material_usage_id"))
                .submitterId(rs.getLong("submitter_id"))
                .orderId(rs.getLong("order_id"))
                .materialName(rs.getString("material_name"))
                .quantity(rs.getInt("quantity"))
                .unitPrice(rs.getBigDecimal("unit_price"))
                .createTime(rs.getTimestamp("create_time").toLocalDateTime())
                .build();
    }
    
}   
