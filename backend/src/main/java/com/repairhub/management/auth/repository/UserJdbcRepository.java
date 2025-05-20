package com.repairhub.management.auth.repository;

import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.auth.entity.UserRowMapper;

@Repository
public class UserJdbcRepository implements UserRepository{
    private final NamedParameterJdbcTemplate jdbc;
    private final UserRowMapper mapper = new UserRowMapper();

    public UserJdbcRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public int insert(User user) {
        String sql = """
          INSERT INTO `users`
            (username, password, email, phone, status, created_at, role)
          VALUES
            (:username, :password, :email, :phone, :status, NOW(), :role)
        """;
        MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("username", user.getUsername())
        .addValue("password", user.getPassword())
        .addValue("email",    user.getEmail())
        .addValue("phone",    user.getPhone())
        // 这里使用 name()，插入 "ACTIVE"/"DISABLED"/"LOCKED"
        .addValue("status",   user.getStatus().name())
        .addValue("role", user.getRole().name());
       return jdbc.update(sql, params);
    }

    @Override
    public Optional<User> findById(Long userId) {
        String sql = "SELECT * FROM `users` WHERE user_id = :userId";
        return jdbc.query(sql, Map.of("userId", userId), mapper)
                   .stream().findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM `users` WHERE username = :username";
        return jdbc.query(sql, Map.of("username", username), mapper)
                   .stream().findFirst();
    }

    @Override
    public int updateStatus(Long userId, String status) {
        String sql = """
          UPDATE `users`
             SET status = :status
           WHERE user_id = :userId
        """;
        return jdbc.update(sql, Map.of("userId", userId, "status", status));
    }
}
