package com.repairhub.management.repair.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.repairhub.management.repair.entity.LaborFeeLog;
import com.repairhub.management.repair.entity.LaborFeeLogRowMapper;

@Repository
public class LaborFeeLogJdbcRepository implements LaborFeeLogRepository{

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final LaborFeeLogRowMapper mapper = new LaborFeeLogRowMapper();

    public LaborFeeLogJdbcRepository(
        NamedParameterJdbcTemplate jdbcTemplate, 
        DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("labor_fee_log")
            .usingGeneratedKeyColumns("labor_fee_log_id");
    }
    
    @Override
    public int insert(LaborFeeLog log){
        SqlParameterSource params = new MapSqlParameterSource()
        .addValue("repairman_id", log.getRepairmanId())
        .addValue("month", log.getMonth().name())
        .addValue("total_hours", log.getTotalHours())
        .addValue("total_income", log.getTotalIncome())
        .addValue("settle_time", log.getSettleTime())
        .addValue("order_id", log.getOrderId());
        Number key = simpleJdbcInsert.executeAndReturnKey(params);
        long laborFeeLogId = key.longValue();
        log.setLaborFeeLogId(laborFeeLogId);
        return 1;
    }

    //TODO : month cannot be simply supported by sql
    @Override
    public int update(LaborFeeLog log){
        String sql = """
            UPDATE labor_fee_log
                SET repairman_id  = :repairmanId,
                     month        = :month,
                     total_hours  = :totalHours,
                     total_income = :totalIncome
                     settle_time  = :settleTime
                     order_id     = :orderId
                WHERE labor_fee_log_id = :logId
                """;
        MapSqlParameterSource source = new MapSqlParameterSource()
        .addValue("repairmanId", log.getRepairmanId())
        .addValue("month", log.getMonth().name())
        .addValue("totalHours", log.getTotalHours())
        .addValue("totalIncome", log.getTotalIncome())
        .addValue("settleTime", log.getSettleTime())
        .addValue("orderId", log.getOrderId())
        .addValue("logId", log.getLaborFeeLogId());
        return jdbcTemplate.update(sql, source);
    }

    @Override
    public int delete(int id){
        String sql = "DELETE FROM labor_fee_log WHERE labor_fee_log_id = :id";
        return jdbcTemplate.update(sql, Map.of("id", id));
    }

    @Override
    public Optional<LaborFeeLog> findById(Long LaborFeeLogId){
        String sql = "SELECT * FROM labor_fee_log WHERE labor_fee_log_id = :laborFeeLogId";
        return jdbcTemplate.query(sql, Map.of("laborFeeLogId", LaborFeeLogId), mapper)
                           .stream()
                           .findFirst();
    }

    @Override
    public List<LaborFeeLog> findAllByRepairOrderId(Long repairOrderId){
        String sql = "SELECT * FROM labor_fee_log WHERE order_id = :repairOrderId";
        return jdbcTemplate.query(sql, Map.of("repairOrderId", repairOrderId), mapper);
    }

    @Override
    public List<LaborFeeLog> findAllByRepairmanId(Long repairmanId){
        String sql = "SELECT * FROM labor_fee_log WHERE repairman_id = :repairmanId";
        return jdbcTemplate.query(sql, Map.of("repairmanId", repairmanId), mapper);
    }

    @Override
    public List<LaborFeeLog> findAll(){
        String sql = "SELECT * FROM labor_fee_log";
        return jdbcTemplate.query(sql, mapper);
    }
}
