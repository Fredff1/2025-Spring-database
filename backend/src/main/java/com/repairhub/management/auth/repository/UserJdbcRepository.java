package com.repairhub.management.auth.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.repairhub.management.auth.domain.enums.UserRole;
import com.repairhub.management.auth.entity.User;
import com.repairhub.management.auth.entity.UserRowMapper;

@Repository
public class UserJdbcRepository implements UserRepository{
    private final NamedParameterJdbcTemplate jdbc;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final UserRowMapper mapper = new UserRowMapper();

    public UserJdbcRepository(
      NamedParameterJdbcTemplate jdbc,
      DataSource dataSource) {
        this.jdbc = jdbc;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).
        withTableName("users")
        .usingGeneratedKeyColumns("user_id")
        .usingColumns("username", "password", "email", "phone", "role", "status");
    }

    @Override
    public int insert(User user) {
        MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("username", user.getUsername())
        .addValue("password", user.getPassword())
        .addValue("email",    user.getEmail())
        .addValue("phone",    user.getPhone())
        .addValue("role",     user.getRole().name())
        .addValue("status",   user.getStatus().name());

        Number key = simpleJdbcInsert.executeAndReturnKey(params);
        long userId = key.longValue();
        user.setUserId(userId);
       return 1;
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

    @Override
    public int updateBasicInfo(Long userId, String username, String phone, String email) {
        String sql = """
            UPDATE `users`
                SET username = :username,
                     phone    = :phone,
                     email    = :email
                 WHERE user_id = :userId
        """;
        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("userId", userId)
            .addValue("username", username)
            .addValue("phone", phone)
            .addValue("email", email);
        return jdbc.update(sql, params);
    }

    @Override
    public List<User> findAllByRole(UserRole role){
        String sql = "SELECT * FROM `users` WHERE role = :role";
        SqlParameterSource params = new MapSqlParameterSource("role", role.name());
        return jdbc.query(sql, params, mapper);
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM `users`";
        return jdbc.query(sql, mapper);
    }
}
