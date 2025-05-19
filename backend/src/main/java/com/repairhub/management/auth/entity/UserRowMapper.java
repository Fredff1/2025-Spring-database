package com.repairhub.management.auth.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.repairhub.management.auth.domain.enums.UserRole;
import com.repairhub.management.auth.domain.enums.UserStatus;

public class UserRowMapper implements RowMapper<User>{
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User u = new User();
        u.setUserId(   rs.getLong("user_id") );
        u.setUsername( rs.getString("username") );
        u.setPassword( rs.getString("password") );
        u.setRole(UserRole.valueOf(rs.getString("role")));
        u.setEmail(    rs.getString("email") );
        u.setPhone(    rs.getString("phone") );
        u.setStatus(   UserStatus.valueOf(rs.getString("status")) );
        u.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        u.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return u;
    }
}
