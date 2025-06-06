package com.repairhub.management.repairman.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.repairhub.management.common.dto.PageResponse;
import com.repairhub.management.repair.entity.LaborFeeLog;
import com.repairhub.management.repair.entity.LaborFeeLogRowMapper;
import com.repairhub.management.utils.PageUtils;

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
    public PageResponse<LaborFeeLog> findFeeLogs(Long repairmanId, LocalDate startTime, LocalDate endTime,int pageNum,int pageSize) {
        long offset = PageUtils.calculateOffset(pageNum, pageSize);
        String sql = """
                SELECT * FROM labor_fee_log
                WHERE repairman_id = :repairmanId
                AND DATE(settle_time) BETWEEN :startTime AND :endTime
                ORDER BY settle_time DESC
                LIMIT :pageSize
                OFFSET :offset
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("repairmanId", repairmanId)
        .addValue("startTime", startTime)
        .addValue("endTime", endTime)
        .addValue("offset", offset)
        .addValue("pageSize", pageSize);
        List<LaborFeeLog> logs = jdbcTemplate.query(sql, params, mapper);
        String countSql = """
                SELECT COUNT(*) FROM labor_fee_log
                WHERE repairman_id = :repairmanId
                AND DATE(settle_time) BETWEEN :startTime AND :endTime
                """;
        Integer total = jdbcTemplate.queryForObject(countSql,params,Integer.class);
        return new PageResponse<>(logs, total); 
    }
    
}
