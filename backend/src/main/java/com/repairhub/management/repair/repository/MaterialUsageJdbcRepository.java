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

import com.repairhub.management.repair.entity.MaterialUsage;
import com.repairhub.management.repair.entity.MaterialUsageRowMapper;

@Repository
public class MaterialUsageJdbcRepository implements MaterialUsageRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final MaterialUsageRowMapper mapper = new MaterialUsageRowMapper();

    public MaterialUsageJdbcRepository(
        NamedParameterJdbcTemplate jdbcTemplate,
        DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("material_usage")
            .usingGeneratedKeyColumns("material_usage_id");
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
                SET
                order_id = :orderId,
                submitter_id = :submitterId,
                material_name = :materialName,
                quantity = :quantity,
                unit_price = :unitPrice
            WHERE material_usage_id = :materialId
                """;
        MapSqlParameterSource source = new MapSqlParameterSource()
        .addValue("orderId", materialUsage.getOrderId())
        .addValue("submitterId", materialUsage.getSubmitterId())
        .addValue("materialName", materialUsage.getMaterialName())
        .addValue("quantity", materialUsage.getQuantity())
        .addValue("unitPrice", materialUsage.getUnitPrice())
        .addValue("materialId", materialUsage.getMaterialId());
        
        return jdbcTemplate.update(sql, source);
    }

    @Override
    public int delete(Long materialId){
        String sql = "DELETE FROM material_usage WHERE material_usage_id = :materialId";
        return jdbcTemplate.update(sql, Map.of("materialId", materialId));
    }

    @Override
    public Optional<MaterialUsage> findById(Long materialId){
        String sql = "SELECT * FROM material_usage WHERE material_usage_id = :materialId";
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

    @Override
    public List<MaterialUsage> findAll(){
        String sql = "SELECT * FROM material_usage";
        return jdbcTemplate.query(sql, mapper);
    }
   
}
