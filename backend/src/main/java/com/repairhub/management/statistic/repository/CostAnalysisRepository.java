package com.repairhub.management.statistic.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CostAnalysisRepository {
    private final NamedParameterJdbcTemplate jdbc;

    public CostAnalysisRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }


    public BigDecimal findTotalMaterialCostAllTime() {
        String sql = """
            SELECT IFNULL(SUM(quantity * unit_price), 0) AS total
            FROM material_usage
            """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        return jdbc.queryForObject(sql, params, BigDecimal.class);
    }

    
    public BigDecimal findTotalLaborCostAllTime() {
        String sql = """
            SELECT IFNULL(SUM(total_income), 0) AS total
            FROM labor_fee_log
            """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        return jdbc.queryForObject(sql, params, BigDecimal.class);
    }


    public BigDecimal findMaterialCostByTime(LocalDateTime since) {
        String sql = """
            SELECT IFNULL(SUM(quantity * unit_price), 0) AS total
            FROM material_usage
            WHERE create_time >= :since
            """;
        MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("since", since);

        return jdbc.queryForObject(sql, params, BigDecimal.class);
    }


    public BigDecimal findLaborCostByTime(LocalDateTime since) {
        String sql = """
            SELECT IFNULL(SUM(total_income), 0) AS total
            FROM labor_fee_log
            WHERE settle_time >= :since
            """;
        MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("since", since);
        return jdbc.queryForObject(sql, params, BigDecimal.class);
    }
}
