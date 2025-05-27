package com.repairhub.management.repair.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.repairhub.management.repair.entity.MaterialUsage;
import com.repairhub.management.repair.entity.MaterialUsageRowMapper;

@Repository
public class MaterialUsageJdbcRepository implements MaterialUsageRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final MaterialUsageRowMapper mapper = new MaterialUsageRowMapper();

    public MaterialUsageJdbcRepository(
        NamedParameterJdbcTemplate jdbcTemplate,
        SimpleJdbcInsert simpleJdbcInsert) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = simpleJdbcInsert
            .withTableName("material_usage")
            .usingGeneratedKeyColumns("material_id");
    }
    
    @Override
    public int insert(MaterialUsage materialUsage){
        SqlParameterSource params = new BeanPropertySqlParameterSource(materialUsage);
        Number key = simpleJdbcInsert.executeAndReturnKey(params);
        long materialId = key.longValue();
        materialUsage.setMaterialId(materialId);
        return 1; 
    }

    @Override
    public int update(MaterialUsage materialUsage){
        String sql = """
            UPDATE material_usage
                SET material_usage_id = :materialId,
                order_id = :orderId,
                material_name = :materialName,
                quantity = :quantity,
                unit_price = :unitPrice,
                """;
        return jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(materialUsage));
    }

    @Override
    public int delete(int materialId){
        String sql = "DELETE FROM material_usage WHERE material_id = :materialId";
        return jdbcTemplate.update(sql, Map.of("materialId", materialId));
    }

    @Override
    public Optional<MaterialUsage> findById(Long materialId){
        String sql = "SELECT * FROM material_usage WHERE material_id = :materialId";
        return jdbcTemplate.query(sql, Map.of("materialId", materialId), mapper)
                           .stream()
                           .findFirst();
    }

    @Override
    public List<MaterialUsage> findByRepairOrderId(Long repairOrderId){
        String sql = "SELECT * FROM material_usage WHERE order_id = :repairOrderId";
        return jdbcTemplate.query(sql, Map.of("repairOrderId", repairOrderId), mapper);
    }

    @Override
    public List<MaterialUsage> findByRepairmanId(Long repairmanId){
        String sql = "SELECT * FROM material_usage WHERE repairman_id = :repairmanId";
        return jdbcTemplate.query(sql, Map.of("repairmanId", repairmanId), mapper);
    }
   
}
