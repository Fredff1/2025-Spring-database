package com.repairhub.management.repair.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.repairhub.management.repair.entity.RepairFeedback;
import com.repairhub.management.repair.entity.RepairFeedbackRowMapper;

@Repository
public class RepairFeedbackJdbcRepository {
    
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RepairFeedbackRowMapper mapper = new RepairFeedbackRowMapper();

    public RepairFeedbackJdbcRepository(
        NamedParameterJdbcTemplate jdbcTemplate,
        SimpleJdbcInsert simpleJdbcInsert) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = simpleJdbcInsert
            .withTableName("feedback")
            .usingGeneratedKeyColumns("feedback_id");
    }

    public int insert(RepairFeedback feedback){
        SqlParameterSource params = new BeanPropertySqlParameterSource(feedback);
        Number key = simpleJdbcInsert.executeAndReturnKey(params);
        long feedbackId = key.longValue();
        feedback.setFeedbackId(feedbackId);
        return 1; 
    }

    public int update(RepairFeedback feedback){
        String sql = """
            UPDATE feedback
               SET repair_order_id = :repairOrderId,
                   repairman_id     = :repairmanId,
                   rating           = :rating,
                   comments         = :comments
             WHERE feedback_id = :feedbackId
            """;
        return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(feedback));
    }

    public int delete(Long repairFeedbackId){
        String sql = "DELETE FROM feedback WHERE feedback_id = :feedbackId";
        return jdbcTemplate.update(sql, Map.of("feedbackId", repairFeedbackId));
    }

    public Optional<RepairFeedback> findById(Long repairFeedbackId){
        String sql = "SELECT * FROM feedback WHERE feedback_id = :feedbackId";
        return jdbcTemplate.query(sql, Map.of("feedbackId", repairFeedbackId), mapper)
                           .stream()
                           .findFirst();
    }

    public Optional<RepairFeedback> findByRepairOrderId(Long repairOrderId){
        String sql = "SELECT * FROM feedback WHERE repair_order_id = :repairOrderId";
        return jdbcTemplate.query(sql, Map.of("repairOrderId", repairOrderId), mapper)
                           .stream()
                           .findFirst();
    }

    public List<RepairFeedback> findByRepairmanId(Long repairmanId){
        String sql = "SELECT * FROM feedback WHERE repairman_id = :repairmanId";
        return jdbcTemplate.query(sql, Map.of("repairmanId", repairmanId), mapper);
    }
}
