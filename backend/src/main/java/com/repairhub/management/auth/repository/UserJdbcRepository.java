package com.repairhub.management.auth.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
      SimpleJdbcInsert simpleJdbcInsert) {
        this.jdbc = jdbc;
        this.simpleJdbcInsert = simpleJdbcInsert.
        withTableName("users")
        .usingGeneratedKeyColumns("user_id");
    }

    @Override
    public int insert(User user) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(user);
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
    public List<User> findAllByRole(UserRole role){
        String sql = "SELECT * FROM `users` WHERE role = :role";
        SqlParameterSource params = new MapSqlParameterSource("role", role.name());
        return jdbc.query(sql, params, mapper);
    }
}
