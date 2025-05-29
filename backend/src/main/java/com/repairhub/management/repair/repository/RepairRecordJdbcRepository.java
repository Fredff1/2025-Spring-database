package com.repairhub.management.repair.repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.repairhub.management.repair.entity.RepairRecord;
import com.repairhub.management.repair.entity.RepairRecordRowMapper;

@Repository
public class RepairRecordJdbcRepository implements RepairRecordRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RepairRecordRowMapper mapper = new RepairRecordRowMapper();

    public RepairRecordJdbcRepository(
        NamedParameterJdbcTemplate jdbcTemplate,
        DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("repair_record")
            .usingGeneratedKeyColumns("repair_record_id");
    }
    // Implement the methods from RepairRecordRepository interface
    @Override
    public int insert(RepairRecord repairRecord) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(repairRecord);
        Number key = simpleJdbcInsert.executeAndReturnKey(params);
        long repairRecordId = key.longValue();
        repairRecord.setRecordId(repairRecordId);
        return 1;
    }

    @Override
    public int update(RepairRecord repairRecord) {
        String sql = """
            UPDATE repair_record
                SET repair_order_id = :repairOrderId,
                     repairman_id     = :repairmanId,
                     fault_type       = :faultType,
                     description      = :description,
                     status           = :status
                """;
        return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(repairRecord));
    }

    @Override
    public int delete(Long repairRecordId) {
        String sql = "DELETE FROM repair_record WHERE repair_record_id = :repairRecordId";
        return jdbcTemplate.update(sql, Map.of("repairRecordId", repairRecordId));

    }

    @Override
    public Optional<RepairRecord> findById(Long repairRecordId) {
        String sql = "SELECT * FROM repair_record WHERE repair_record_id = :repairRecordId";
        return jdbcTemplate.query(sql, Map.of("repairRecordId", repairRecordId), mapper)
                            .stream()
                            .findFirst();
    }

    @Override
    public List<RepairRecord> findByRepairOrderId(Long repairOrderId) {
        String sql = "SELECT * FROM repair_record WHERE repair_order_id = :repairOrderId";
        return jdbcTemplate.query(sql, Map.of("repairOrderId", repairOrderId), mapper);
    }

    @Override
    public List<RepairRecord> findByRepairmanId(Long repairmanId) {
        String sql = "SELECT * FROM repair_record WHERE repairman_id = :repairmanId";
        return jdbcTemplate.query(sql, Map.of("repairmanId", repairmanId), mapper);
    }

    @Override
    public int countByUserId(Long userId) {
        String sql = """
                    SELECT COUNT(*) FROM repair_record r 
                    JOIN repair_order o ON r.order_id = o.order_id 
                    WHERE o.user_id = :userId
                    """;
        return jdbcTemplate.queryForObject(
            sql,
            Collections.singletonMap("userId", userId),
            Integer.class
        );
    }
    
}
