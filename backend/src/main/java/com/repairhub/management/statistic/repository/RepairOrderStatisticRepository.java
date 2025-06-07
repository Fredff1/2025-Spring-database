package com.repairhub.management.statistic.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.repairhub.management.repair.enums.FaultType;
import com.repairhub.management.statistic.dto.MismatchRepairTypeDTO;
import com.repairhub.management.statistic.dto.OrderProcessStatDTO;
import com.repairhub.management.statistic.dto.UnfinishedOrderFaultTypeStatsDTO;
import com.repairhub.management.statistic.dto.UnfinishedRepairmanStatsDTO;
import com.repairhub.management.statistic.dto.UnfinishedVehicleStatsDTO;

@Repository
public class RepairOrderStatisticRepository {
    private final NamedParameterJdbcTemplate jdbc;

    public RepairOrderStatisticRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<UnfinishedOrderFaultTypeStatsDTO> unfinishedOrderFaultTypeMapper =UnfinishedOrderFaultTypeStatsDTO.getRowMapper();

    private final RowMapper<UnfinishedRepairmanStatsDTO> unfinishedRepairmanMapper = UnfinishedRepairmanStatsDTO.getRowMapper();

    private final RowMapper<UnfinishedVehicleStatsDTO> unfinishedVehicleMapper = UnfinishedVehicleStatsDTO.getRowMapper();

    private final RowMapper<OrderProcessStatDTO> orderProcessStatMapper = new RowMapper<OrderProcessStatDTO>() {
        @Override
        public OrderProcessStatDTO mapRow(ResultSet rs, int rowNum) throws SQLException{
            return OrderProcessStatDTO.builder()
            .pendingOrders(rs.getLong("pending_orders"))
            .completedOrders(rs.getLong("completed_orders"))
            .processingOrders(rs.getLong("processing_orders"))
            .totalOrders(rs.getLong("total_orders"))
            .faultType(FaultType.valueOf(rs.getString("fault_type")))
            .build();
        }
    };

    private final RowMapper<MismatchRepairTypeDTO> mismatchRepairRowMapper = new RowMapper<MismatchRepairTypeDTO>() {
        @Override
        public MismatchRepairTypeDTO mapRow(ResultSet rs, int rowNum) throws SQLException{
            return MismatchRepairTypeDTO.builder()
            .repairRecordId(rs.getLong("repair_record_id"))
            .orderId(rs.getLong("order_id"))
            .repairmanName(rs.getString("repairman_name"))
            .repairmanSpecialty(FaultType.valueOf(rs.getString("repairman_specialty")))
            .orderFaultType(FaultType.valueOf(rs.getString("order_fault_type")))
            .completionTime(rs.getTimestamp("completion_time").toLocalDateTime())
            .build();
        }
    };

    public List<OrderProcessStatDTO> findProcessStatDTO(LocalDate begin,LocalDate end){
        String sql = """
            SELECT
              ro.fault_type AS fault_type,
              COUNT(*) AS total_orders,
              SUM(CASE WHEN ro.status = 'COMPLETED' THEN 1 ELSE 0 END) AS completed_orders,
              SUM(CASE WHEN ro.status = 'PROCESSING' THEN 1 ELSE 0 END) AS processing_orders,
              SUM(CASE WHEN ro.status = 'PENDING' THEN 1 ELSE 0 END) AS pending_orders
              FROM repair_order ro
            WHERE ro.submit_time BETWEEN :begin AND :end
            GROUP BY ro.fault_type
            ORDER BY total_orders DESC
        """;
        MapSqlParameterSource params = new MapSqlParameterSource()
        .addValue("begin", begin)
        .addValue("end", end);
        return jdbc.query(sql,params,orderProcessStatMapper);
    }

    public List<MismatchRepairTypeDTO> findMismatchRecord(){
        String sql = """
            SELECT
              rr.repair_record_id,
              u.username AS repairman_name,
              rp.specialty AS repairman_specialty,
              ro.fault_type AS order_fault_type,
              ro.order_id,
              rr.completion_time
            FROM repair_record rr
            JOIN users u 
              ON rr.repairman_id = u.user_id
            JOIN repairman_profile rp 
              ON u.user_id = rp.user_id
            JOIN repair_order ro 
              ON rr.order_id = ro.order_id
            WHERE ro.fault_type <> rp.specialty COLLATE utf8mb4_unicode_ci
            ORDER BY rr.completion_time DESC;
            """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        return jdbc.query(sql, params, mismatchRepairRowMapper);
    }

    public List<UnfinishedOrderFaultTypeStatsDTO> findUnfinishedOrderFaultTypeStats(){
        String sql = """
            SELECT
              ro.fault_type AS fault_type,
              COUNT(*) AS count
            FROM repair_order ro
            WHERE ro.status in ('PENDING','PROCESSING')
            GROUP BY ro.fault_type
            ORDER BY count DESC
        """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        return jdbc.query(sql, params, unfinishedOrderFaultTypeMapper);
    }

    public List<UnfinishedRepairmanStatsDTO> findUnfinishedOrderRepairmanStats(){
        String sql = """
            SELECT
              COUNT(*) as count,
              u.username as repairman_name,
              rp.specialty as specialty
            FROM assignment a 
            JOIN users u 
              ON u.user_id = a.repairman_id
            JOIN repairman_profile rp 
              ON u.user_id = rp.user_id
            JOIN repair_order ro ON ro.order_id = a.order_id
            WHERE ro.status IN ('PENDING', 'PROCESSING')
              AND a.assignment_status = 'ACCEPTED'
            GROUP BY u.user_id, u.username, rp.specialty
            ORDER BY count DESC
        """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        return jdbc.query(sql, params, unfinishedRepairmanMapper);
    }

    public List<UnfinishedVehicleStatsDTO> findUnfinishedOrderVehicleStats(){
        String sql = """
            SELECT
              v.license_plate as license_plate,
              v.brand as brand,
              v.model as model,
              COUNT(*) as count
            FROM repair_order ro
            JOIN vehicle v
              ON ro.vehicle_id = v.vehicle_id
            WHERE ro.status IN ('PENDING', 'PROCESSING')
            GROUP BY v.vehicle_id, v.license_plate, v.brand, v.model
            ORDER BY count DESC
        """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        return jdbc.query(sql, params, unfinishedVehicleMapper);
    }
}
