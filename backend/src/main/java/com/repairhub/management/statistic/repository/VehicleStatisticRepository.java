package com.repairhub.management.statistic.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.repairhub.management.order.enums.OrderStatus;
import com.repairhub.management.repair.enums.FaultType;
import com.repairhub.management.statistic.dto.UnfinishedOrderFaultTypeStatsDTO;
import com.repairhub.management.statistic.dto.UnfinishedRepairmanStatsDTO;
import com.repairhub.management.statistic.dto.UnfinishedVehicleStatsDTO;
import com.repairhub.management.statistic.dto.VehicleModelFaultTypeCountDTO;
import com.repairhub.management.statistic.dto.VehicleRepairStatsDTO;

@Repository
public class VehicleStatisticRepository {
    
    private final NamedParameterJdbcTemplate jdbc;

    public VehicleStatisticRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<VehicleRepairStatsDTO> vehicleStatsMapper = new RowMapper<>() {
        @Override
        public VehicleRepairStatsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return VehicleRepairStatsDTO.builder()
                    .model(rs.getString("model"))
                    .repairCount(rs.getLong("repair_count"))
                    .averageCost(rs.getBigDecimal("avg_cost"))
                    .build();
        }
    };

    private final RowMapper<VehicleModelFaultTypeCountDTO> faultTypeMapper = new RowMapper<>() {
        @Override
        public VehicleModelFaultTypeCountDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return VehicleModelFaultTypeCountDTO.builder()
                    .model(rs.getString("model"))
                    .faultType(FaultType.valueOf(rs.getString("fault_type")))
                    .count(rs.getLong("count"))
                    .build();
        }
    };

    

    public List<VehicleRepairStatsDTO> findAllVehicleRepairStats(){
        String sql = """
                SELECT 
                  v.model AS model,
                  COUNT(ro.order_id) AS repair_count,
                  ROUND(AVG(ro.total_fee),2) AS avg_cost
                FROM repair_order ro
                JOIN vehicle v
                  ON ro.vehicle_id = v.vehicle_id
                WHERE ro.status = 'COMPLETED'
                GROUP BY v.model
                ORDER BY repair_count DESC
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        return jdbc.query(sql, params, vehicleStatsMapper);

    }

    public List<VehicleRepairStatsDTO> findVehicleRepairStatsByTime(LocalDate since){
        String sql = """
                SELECT 
                  v.model AS model,
                  COUNT(ro.order_id) AS repair_count,
                  ROUND(AVG(ro.total_fee),2) AS avg_cost
                FROM repair_order ro
                JOIN vehicle v
                  ON ro.vehicle_id = v.vehicle_id
                WHERE ro.status = 'COMPLETED'
                  AND ro.submit_time < :since
                GROUP BY v.model
                ORDER BY repair_count DESC
                """;
        
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("since", since);
        return jdbc.query(sql, params, vehicleStatsMapper);

    }

    public List<VehicleModelFaultTypeCountDTO> findAllFaultTypeCount(){
        String sql = """
                SELECT 
                  ro.fault_type AS fault_type,
                  COUNT(*) AS count,
                  v.model AS model
                FROM repair_order ro
                JOIN vehicle v
                  ON ro.vehicle_id = v.vehicle_id
                GROUP BY v.model, ro.fault_type
                ORDER BY count DESC
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        return jdbc.query(sql, params, faultTypeMapper);
    }

    

    public List<VehicleModelFaultTypeCountDTO> findFaultTypeCountByTime(LocalDate since){
         String sql = """
                SELECT 
                  ro.fault_type AS fault_type,
                  COUNT(*) AS count,
                  v.model AS model
                FROM repair_order ro
                JOIN vehicle v
                  ON ro.vehicle_id = v.vehicle_id
                WHERE ro.submit_time < :since
                GROUP BY v.model, ro.fault_type
                ORDER BY count DESC
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("since", since);
        return jdbc.query(sql, params, faultTypeMapper);
    }

    

    

}
