package com.repairhub.management.auth.repository;

import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
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
          INSERT INTO `user`
            (username, password, email, phone, status, created_at)
          VALUES
            (:username, :password, :email, :phone, :status, NOW())
        """;
        return jdbc.update(sql, new BeanPropertySqlParameterSource(user));
    }

    @Override
    public Optional<User> findById(Long userId) {
        String sql = "SELECT * FROM `user` WHERE user_id = :userId";
        return jdbc.query(sql, Map.of("userId", userId), mapper)
                   .stream().findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM `user` WHERE username = :username";
        return jdbc.query(sql, Map.of("username", username), mapper)
                   .stream().findFirst();
    }

    @Override
    public int updateStatus(Long userId, String status) {
        String sql = """
          UPDATE `user`
             SET status = :status
           WHERE user_id = :userId
        """;
        return jdbc.update(sql, Map.of("userId", userId, "status", status));
    }
}
