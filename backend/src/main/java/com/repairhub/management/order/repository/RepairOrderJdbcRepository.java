package com.repairhub.management.order.repository;

import java.util.Collections;
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

import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.order.entity.RepairOrderRowMapper;

@Repository
public class RepairOrderJdbcRepository implements RepairOrderRepository{

    private final NamedParameterJdbcTemplate jdbc;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RepairOrderRowMapper mapper = new RepairOrderRowMapper();

    public RepairOrderJdbcRepository(
        NamedParameterJdbcTemplate jdbc,
       DataSource dataSource) {
        this.jdbc = jdbc;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("repair_order")
            .usingGeneratedKeyColumns("order_id");
    }


    @Override
    public int insert(RepairOrder order){
        SqlParameterSource params = new BeanPropertySqlParameterSource(order);
        Number key = simpleJdbcInsert.executeAndReturnKey(params);
        long orderId = key.longValue();
        order.setOrderId(orderId);
        return 1;
    }

    @Override
    public int update(RepairOrder order){
        String sql = """
                UPDATE repair_order
                   SET user_id      = :userId,
                       vehicle_id   = :vehicleId,
                       total_fee    = :totalFee,
                       update_time  = :updateTime,
                       submit_time  = :submitTime,
                       status       = :status,
                       fault_type   = :faultType,
                       is_paid      = :isPaid,
                       description  = :description
                 WHERE order_id = :orderId
                """;
        SqlParameterSource params = new MapSqlParameterSource()
        .addValue("orderId", order.getOrderId())
        .addValue("userId", order.getUserId())
        .addValue("vehicleId", order.getVehicleId())
        .addValue("totalFee", order.getTotalFee())
        .addValue("updateTime", order.getUpdateTime())
        .addValue("submitTime", order.getSubmitTime())
        .addValue("status", order.getStatus().name())
        .addValue("faultType", order.getFaultType().name())
        .addValue("isPaid", order.getIsPaid())
        .addValue("description", order.getDescription());
        return jdbc.update(sql, params);
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
    public List<RepairOrder> findByUserId(Long userId){
        String sql = "SELECT * FROM repair_order WHERE user_id = :userId";
        return jdbc.query(sql, Map.of("userId", userId), mapper);
    }

    @Override
    public List<RepairOrder> findByUserIdAndVehicleId(Long userId, Long vehicleId){
        String sql = "SELECT * FROM repair_order WHERE user_id = :userId AND vehicle_id = :vehicleId";
        return jdbc.query(sql, Map.of("userId", userId, "vehicleId", vehicleId), mapper);
    }

    @Override
    public int countByUserId(Long userId){
        String sql = "SELECT COUNT(*) FROM repair_order WHERE user_id = :userId";
        return jdbc.queryForObject(
            sql,
            Collections.singletonMap("userId", userId),
            Integer.class
        );
    }
}
