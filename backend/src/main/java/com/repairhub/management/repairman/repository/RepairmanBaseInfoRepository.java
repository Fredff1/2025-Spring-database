package com.repairhub.management.repairman.repository;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.repairhub.management.repairman.dto.RepairmanBaseInfoDTO;
import com.repairhub.management.repairman.entity.RepairmanProfileRowMapper;

@Repository
public class RepairmanBaseInfoRepository {
    private final NamedParameterJdbcTemplate jdbc;
    private final RowMapper<RepairmanBaseInfoDTO> rowMapper = RepairmanBaseInfoDTO.getRowMapper();

    public RepairmanBaseInfoRepository(
        NamedParameterJdbcTemplate jdbc,
        DataSource dataSource) {
        this.jdbc = jdbc;
    }

    public List<RepairmanBaseInfoDTO> findAllByOrderId(Long orderId){
        String sql = """
            SELECT DISTINCT
              u.username AS repairman_name,
              rp.user_id AS repairman_id,
              rp.repairman_number AS repairman_number
            FROM repairman_profile rp
            JOIN users u
              ON u.user_id = rp.user_id
            JOIN assignment asg 
              ON asg.repairman_id = u.user_id
            JOIN repair_order ro
              ON ro.order_id = asg.order_id
            WHERE asg.assignment_status = 'ACCEPTED'
              AND ro.order_id = :orderId
            
        """;
        SqlParameterSource params = new MapSqlParameterSource()
        .addValue("orderId", orderId);
        return jdbc.query(sql, params, rowMapper);
    }
}
