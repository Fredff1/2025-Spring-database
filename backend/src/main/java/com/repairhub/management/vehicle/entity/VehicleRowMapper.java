package com.repairhub.management.vehicle.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class VehicleRowMapper implements RowMapper<Vehicle> {

    @Override
    public Vehicle mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Vehicle.builder()
                .vehicleId(rs.getLong("vehicle_id"))
                .ownerId(rs.getLong("owner_id"))
                .brand(rs.getString("brand"))
                .model(rs.getString("model"))
                .licensePlate(rs.getString("license_plate"))
                .registerDate(rs.getDate("register_date").toLocalDate())
                .build();
    }
    
}
