package com.repairhub.management.repairman.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.repairhub.management.repair.entity.LaborFeeLog;
import com.repairhub.management.repair.entity.LaborFeeLogRowMapper;

@Repository
public class RepairmanIncomeJdbcRepository implements RepairmanIncomeRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private LaborFeeLogRowMapper mapper = new LaborFeeLogRowMapper();

    public RepairmanIncomeJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public BigDecimal sumTotalIncome(Long repairmanId, LocalDate startTime, LocalDate endTime) {
        String sql = """
                SELECT COALESCE(SUM(total_income), 0)
                FROM labor_fee_log
                WHERE repairman_id = :repairmanId
                AND DATE(settle_time) BETWEEN :startTime AND :endTime
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("repairmanId", repairmanId)
        .addValue("startTime", startTime)
        .addValue("endTime", endTime);
        BigDecimal totalIncome = jdbcTemplate.queryForObject(sql, params, BigDecimal.class);
        return totalIncome; 
    }

    @Override
    public BigDecimal sumTotalHours(Long repairmanId, LocalDate startTime, LocalDate endTime) {
        String sql = """
                SELECT COALESCE(SUM(total_hours), 0)
                FROM labor_fee_log
                WHERE repairman_id = :repairmanId
                AND DATE(settle_time) BETWEEN :startTime AND :endTime
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("repairmanId", repairmanId)
        .addValue("startTime", startTime)
        .addValue("endTime", endTime);
        BigDecimal totalHours = jdbcTemplate.queryForObject(sql, params, BigDecimal.class);
        return totalHours; 
    }

    @Override
    public List<LaborFeeLog> findFeeLogs(Long repairmanId, LocalDate startTime, LocalDate endTime) {
        String sql = """
                SELECT * FROM labor_fee_log
                WHERE repairman_id = :repairmanId
                AND DATE(settle_time) BETWEEN :startTime AND :endTime
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("repairmanId", repairmanId)
        .addValue("startTime", startTime)
        .addValue("endTime", endTime);
        List<LaborFeeLog> logs = jdbcTemplate.query(sql, params, mapper);
        return logs; 
    }
    
}
