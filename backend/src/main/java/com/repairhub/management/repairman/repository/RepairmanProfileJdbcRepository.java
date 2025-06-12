package com.repairhub.management.repairman.repository;

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

import com.repairhub.management.repair.enums.FaultType;
import com.repairhub.management.repairman.dto.RepairmanProfileUpdateDTO;
import com.repairhub.management.repairman.entity.RepairmanProfile;
import com.repairhub.management.repairman.entity.RepairmanProfileRowMapper;



@Repository
public class RepairmanProfileJdbcRepository implements RepairmanProfileRepository{
    
    private final NamedParameterJdbcTemplate jdbc;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private RepairmanProfileRowMapper mapper = new RepairmanProfileRowMapper();

    public RepairmanProfileJdbcRepository(
        NamedParameterJdbcTemplate jdbc,
        DataSource dataSource) {
        this.jdbc = jdbc;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("repairman_profile")
            .usingColumns("user_id","specialty","hourly_money_rate");
    }

    @Override
    public int insert(RepairmanProfile profile){
        MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("user_id", profile.getUserId())
        .addValue("specialty", profile.getSpecialty().name())
        .addValue("hourly_money_rate", profile.getHourlyMoneyRate())
        .addValue("repairman_number", profile.getRepairmanNumber());
        simpleJdbcInsert.execute(params);
        return 1;
    }

    @Override
    public int update(Long userId,RepairmanProfileUpdateDTO profile){
        String sql = """
            UPDATE repairman_profile
               SET specialty         = :specialty,
                   hourly_money_rate = :hourlyMoneyRate      
             WHERE user_id = :userId
            """;

        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("userId",userId)
            .addValue("specialty", profile.getSpecialty().name())
            .addValue("hourlyMoneyRate", profile.getHourlyMoneyRate());

        return jdbc.update(sql, params);
    }

    @Override
    public List<RepairmanProfile> findAll(){
        String sql = "SELECT * FROM repairman_profile";
        return jdbc.query(sql, mapper);
    }

    @Override
    public Optional<RepairmanProfile> findByUserId(Long userId){
        String sql = "SELECT * FROM repairman_profile WHERE user_id = :userId";
        return jdbc.query(sql, Map.of("userId", userId), mapper).stream().findFirst();
    }

    @Override
    public List<RepairmanProfile> findBySpecialty(FaultType specialty){
        String sql = "SELECT * FROM repairman_profile WHERE specialty = :specialty";
        return jdbc.query(sql, Map.of("specialty", specialty.name()), mapper);
    }

    @Override
    public Optional<RepairmanProfile> findByRepairmanNumber(String number){
        String sql = "SELECT * FROM repairman_profile WHERE repairman_number = :repairmanNumber";
        return jdbc.query(sql, Map.of("repairmanNumber", number), mapper).stream().findFirst();
    }
}
