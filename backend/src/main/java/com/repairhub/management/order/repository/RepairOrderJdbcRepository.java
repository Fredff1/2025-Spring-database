package com.repairhub.management.order.repository;

import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.order.entity.RepairOrderRowMapper;

@Repository
public class RepairOrderJdbcRepository implements RepairOrderRepository{

    private final NamedParameterJdbcTemplate jdbc;
    private final RepairOrderRowMapper mapper = new RepairOrderRowMapper();

    public RepairOrderJdbcRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }


    @Override
    public int insert(RepairOrder order){
        String sql = """
                INSERT INTO repair_order
                    (user_id, vehicle_id, submit_time, status, description)
                VALUES
                    (:userId, :vehicleId, :submitTime, :status, :description)
                """;
        return jdbc.update(sql, new BeanPropertySqlParameterSource(order));
    }

    @Override
    public int update(RepairOrder order){
        String sql = """
                UPDATE repair_order
                   SET user_id      = :userId,
                       vehicle_id   = :vehicleId,
                       submit_time  = :submitTime,
                       status       = :status,
                       description  = :description
                 WHERE order_id = :orderId
                """;
        return jdbc.update(sql, new BeanPropertySqlParameterSource(order));
    }

    @Override
    public int deleteById(Long orderId){
        String sql = "DELETE FROM repair_order WHERE order_id = :orderId";
        return jdbc.update(sql, Map.of("orderId", orderId));
    }

    @Override
    public Optional<RepairOrder> findById(Long orderId){
        String sql = "SELECT * FROM repair_order WHERE order_id = :orderId";
        return jdbc.query(sql, Map.of("orderId", orderId), mapper)
                   .stream().findFirst();
    }

    @Override
    public Optional<RepairOrder> findByUserIdAndVehicleId(Long userId, Long vehicleId){
        String sql = "SELECT * FROM repair_order WHERE user_id = :userId AND vehicle_id = :vehicleId";
        return jdbc.query(sql, Map.of("userId", userId, "vehicleId", vehicleId), mapper)
                   .stream().findFirst();
    }
}
