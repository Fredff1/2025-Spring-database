package com.repairhub.management.repairman.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RepairmanDashboardJdbcRepository implements RepairmanDashboardRepository{

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public RepairmanDashboardJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int countTotalAssignments(Long repairmanId, LocalDateTime startOfToday, LocalDateTime now) {
        String sql = """
            SELECT COUNT(*) 
              FROM assignment
             WHERE repairman_id = :repairmanId
               AND assignment_time BETWEEN :start AND :end
               AND assignment_status = 'PENDING'
        """;
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("repairmanId", repairmanId)
            .addValue("start",       startOfToday)
            .addValue("end",         now);

        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return (count == null ? 0 : count);
    }

    @Override
    public int countProcessingOrders(Long repairmanId) {
        String sql = """
            SELECT COUNT(*) 
                FROM repair_order o
                JOIN assignment a
                    ON o.order_id = a.order_id
            WHERE a.repairman_id = :repairmanId
                AND a.assignment_status = 'ACCEPTED'
                AND o.status = 'PROCESSING'
        """;
        Integer count = jdbcTemplate.queryForObject(
            sql,
            Map.of("repairmanId", repairmanId),
            Integer.class
        );
        return (count == null ? 0 : count);
    }

    @Override
    public BigDecimal sumMonthlyIncome(Long repairmanId, LocalDateTime startOfMonth, LocalDateTime now) {
        String sql = """
            SELECT COALESCE(SUM(total_income), 0)
              FROM labor_fee_log
             WHERE repairman_id = :repairmanId
               AND settle_time BETWEEN :start AND :end
        """;
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("repairmanId", repairmanId)
            .addValue("start",       startOfMonth)
            .addValue("end",         now);

        BigDecimal sum = jdbcTemplate.queryForObject(sql, params, BigDecimal.class);
        return (sum == null ? BigDecimal.ZERO : sum);
    }

    @Override
    public Double averageRatingAllTime(Long repairmanId) {
        String sql = """
            SELECT COALESCE(AVG(f.rating), 0)
                FROM feedback f
                JOIN repair_order o
                    ON f.order_id = o.order_id
                JOIN assignment a
                    ON o.order_id = a.order_id
            WHERE a.repairman_id = :repairmanId
                AND a.assignment_status = 'ACCEPTED'
        """;
        Double avg = jdbcTemplate.queryForObject(
            sql,
            Map.of("repairmanId", repairmanId),
            Double.class
        );
        return (avg == null ? 0.0 : avg);
    }


    // ——— “统计”所需的 4 个指标 ———

    @Override
    public int countCompletedOrdersAfter(Long repairmanId, LocalDateTime startTime) {
        String sql = """
            SELECT COUNT(*) 
                FROM repair_order o
                JOIN assignment a
                    ON o.order_id = a.order_id
            WHERE a.repairman_id = :repairmanId
                AND a.assignment_status = 'ACCEPTED'
                AND o.status = 'COMPLETED'
                AND o.submit_time > :startTime
        """;
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("repairmanId", repairmanId)
            .addValue("startTime",   startTime);

        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return (count == null ? 0 : count);
    }

    @Override
    public BigDecimal sumWorkHoursAfter(Long repairmanId, LocalDateTime startTime) {
        String sql = """
            SELECT COALESCE(SUM(actual_work_hours), 0)
              FROM repair_record
             WHERE repairman_id = :repairmanId
               AND completion_time > :startTime
        """;
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("repairmanId", repairmanId)
            .addValue("startTime",   startTime);

        BigDecimal sum = jdbcTemplate.queryForObject(sql, params, BigDecimal.class);
        return (sum == null ? BigDecimal.ZERO : sum);
    }

    @Override
    public BigDecimal sumIncomeAfter(Long repairmanId, LocalDateTime startTime) {
        String sql = """
            SELECT COALESCE(SUM(total_income), 0)
              FROM labor_fee_log
             WHERE repairman_id = :repairmanId
               AND settle_time > :startTime
        """;
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("repairmanId", repairmanId)
            .addValue("startTime",   startTime);

        BigDecimal sum = jdbcTemplate.queryForObject(sql, params, BigDecimal.class);
        return (sum == null ? BigDecimal.ZERO : sum);
    }

    @Override
    public Double averageRatingAfter(Long repairmanId, LocalDateTime startTime) {
        String sql = """
            SELECT COALESCE(AVG(f.rating), 0)
              FROM feedback f
              JOIN repair_order o
                ON f.order_id = o.order_id
              JOIN assignment a
                ON o.order_id = a.order_id
            WHERE a.repairman_id = :repairmanId
                AND a.assignment_status = 'ACCEPTED'
                AND f.feedback_time > :startTime
        """;
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("repairmanId", repairmanId)
            .addValue("startTime",   startTime);

        Double avg = jdbcTemplate.queryForObject(sql, params, Double.class);
        return (avg == null ? 0.0 : avg);
    }

}
