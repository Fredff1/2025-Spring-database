package com.repairhub.management.admin.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.order.entity.RepairOrderRowMapper;

@Repository
public class AdminOverviewRepository {
    private final NamedParameterJdbcTemplate jdbc;
    private final RepairOrderRowMapper orderRowMapper = new RepairOrderRowMapper();

    public AdminOverviewRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<RepairOrder> findUnfinishedOrders(Integer limit){
        String sql = """
            SELECT * 
            FROM repair_order
            WHERE status IN ('PENDING','PROCESSING')
            ORDER BY submit_time DESC
            LIMIT :limit
        """;
        SqlParameterSource params = new MapSqlParameterSource()
        .addValue("limit", limit);
        return jdbc.query(sql, params, orderRowMapper);
    }

    public Integer countTodayOrders(){
        String sql = """
            SELECT COUNT(*)
            FROM repair_order
            WHERE submit_time > :date
        """;
        SqlParameterSource params = new MapSqlParameterSource()
        .addValue("date", LocalDateTime.now().minusDays(1L));
        return jdbc.queryForObject(sql, params, Integer.class);
    }

    public Integer countUnfinishedOrders(){
        String sql = """
            SELECT COUNT(*)
            FROM repair_order
            WHERE status IN ('PENDING','PROCESSING')
        """;
        SqlParameterSource params = new MapSqlParameterSource();
        return jdbc.queryForObject(sql, params, Integer.class);
    }

    public BigDecimal countMonthlyCost(){
        String sql = """
            SELECT IFNULL(SUM(total_fee), 0)
            FROM repair_order
            WHERE status = 'COMPLETED'
              AND submit_time > :date
        """;
        SqlParameterSource params = new MapSqlParameterSource()
        .addValue("date", LocalDateTime.now().minusMonths(1L));
        return jdbc.queryForObject(sql, params, BigDecimal.class);
    }

    public Integer countActiveRepairman(){
        String sql = """
            SELECT COUNT(DISTINCT a.repairman_id)
            FROM assignment a
            JOIN repair_order ro
              ON ro.order_id = a.order_id
            WHERE ro.status = 'PROCESSING'
              AND a.assignment_status = 'ACCEPTED'
        """;
        SqlParameterSource params = new MapSqlParameterSource();
        return jdbc.queryForObject(sql, params, Integer.class);
    }
}
