package com.repairhub.management.auth.repository;

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

import com.repairhub.management.auth.entity.RepairmanProfile;
import com.repairhub.management.auth.entity.RepairmanProfileRowMapper;
import com.repairhub.management.repair.enums.FaultType;



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
        .addValue("hourly_money_rate", profile.getHourlyMoneyRate());
        simpleJdbcInsert.execute(params);
        return 1;
    }

    @Override
    public int update(RepairmanProfile profile){
        String sql = """
            UPDATE repairman_profile
               SET specialty         = :specialty,
                   hourly_money_rate = :hourlyMoneyRate
             WHERE user_id = :userId
            """;
        return jdbc.update(sql, new BeanPropertySqlParameterSource(profile));
    }

    @Override
    public List<RepairmanProfile> findAll(){
        String sql = "SELECT user_id, specialty, hourly_money_rate FROM repairman_profile";
        return jdbc.query(sql, mapper);
    }

    @Override
    public Optional<RepairmanProfile> findByUserId(Long userId){
        String sql = "SELECT user_id, specialty, hourly_money_rate FROM repairman_profile WHERE user_id = :userId";
        return jdbc.query(sql, Map.of("userId", userId), mapper).stream().findFirst();
    }

    @Override
    public List<RepairmanProfile> findBySpecialty(FaultType specialty){
        String sql = "SELECT user_id, specialty, hourly_money_rate FROM repairman_profile WHERE specialty = :specialty";
        return jdbc.query(sql, Map.of("specialty", specialty.name()), mapper);
    }
}
