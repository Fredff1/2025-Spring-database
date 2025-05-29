package com.repairhub.management.repair.repository;

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

import com.repairhub.management.repair.entity.RepairFeedback;
import com.repairhub.management.repair.entity.RepairFeedbackRowMapper;

@Repository
public class RepairFeedbackJdbcRepository implements RepairFeedbackRepository{
    
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RepairFeedbackRowMapper mapper = new RepairFeedbackRowMapper();

    public RepairFeedbackJdbcRepository(
        NamedParameterJdbcTemplate jdbcTemplate,
        DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("feedback")
            .usingGeneratedKeyColumns("feedback_id");
    }

    @Override
    public int insert(RepairFeedback feedback){
        SqlParameterSource params = new MapSqlParameterSource()
        .addValue("order_id", feedback.getOrderId())
        .addValue("user_id", feedback.getUserId())
        .addValue("rating", feedback.getRating())
        .addValue("feed_back_type", feedback.getFeedbackType().name())
        .addValue("description", feedback.getDescription())
        .addValue("feedback_time", feedback.getFeedbackTime());
        Number key = simpleJdbcInsert.executeAndReturnKey(params);
        long feedbackId = key.longValue();
        feedback.setFeedbackId(feedbackId);
        return 1; 
    }

    @Override
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

    @Override
    public int delete(Long repairFeedbackId){
        String sql = "DELETE FROM feedback WHERE feedback_id = :feedbackId";
        return jdbcTemplate.update(sql, Map.of("feedbackId", repairFeedbackId));
    }

    @Override
    public Optional<RepairFeedback> findById(Long repairFeedbackId){
        String sql = "SELECT * FROM feedback WHERE feedback_id = :feedbackId";
        return jdbcTemplate.query(sql, Map.of("feedbackId", repairFeedbackId), mapper)
                           .stream()
                           .findFirst();
    }

    @Override
    public List<RepairFeedback> findByRepairOrderId(Long repairOrderId){
        String sql = "SELECT * FROM feedback WHERE order_id = :order_id";
        return jdbcTemplate.query(sql, Map.of("order_id", repairOrderId), mapper);
    }

    @Override
    public List<RepairFeedback> findByRepairmanId(Long repairmanId){
        String sql = "SELECT * FROM feedback WHERE repairman_id = :repairmanId";
        return jdbcTemplate.query(sql, Map.of("repairmanId", repairmanId), mapper);
    }
}
