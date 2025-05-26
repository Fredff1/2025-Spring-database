package com.repairhub.management.order.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.repairhub.management.order.entity.OrderAssignment;
import com.repairhub.management.order.entity.OrderAssignmentRowMapper;

@Repository
public class OrderAssignmentJdbcRepository implements OrderAssignmentRepository{
    
    private final NamedParameterJdbcTemplate jdbc;
    private final OrderAssignmentRowMapper mapper = new OrderAssignmentRowMapper();

    public OrderAssignmentJdbcRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // 新增一条订单分配记录
    @Override
    public int insert(OrderAssignment orderAssignment){
        String sql = """
            INSERT INTO order_assignment
              (order_id, repairman_id, assignment_time, status)
            VALUES
              (:orderId, :repairmanId, :assignmentTime, :status)
            """;
        return jdbc.update(sql, new BeanPropertySqlParameterSource(orderAssignment));
    }

    // 更新订单分配记录（如接受或拒绝）
    @Override
    public int update(OrderAssignment orderAssignment){
        String sql = """
            UPDATE order_assignment
               SET repairman_id    = :repairmanId,
                   assignment_time = :assignmentTime,
                   status          = :status
             WHERE assignment_id = :assignmentId
            """;
        return jdbc.update(sql, new BeanPropertySqlParameterSource(orderAssignment));
    }

    // 删除订单分配记录
    @Override
    public int deleteById(Long assignmentId){
        String sql = "DELETE FROM order_assignment WHERE assignment_id = :assignmentId";
        return jdbc.update(sql, Map.of("assignmentId", assignmentId));
    }

    // 根据主键查找订单分配记录
    @Override
    public Optional<OrderAssignment> findById(Long assignmentId){
        String sql = "SELECT * FROM order_assignment WHERE assignment_id = :assignmentId";
        return jdbc.query(sql, Map.of("assignmentId", assignmentId), mapper)
                   .stream().findFirst();
    }

    // 查询某个订单的所有分配记录
    @Override
    public List<OrderAssignment> findByOrderId(Long orderId){
        String sql = "SELECT * FROM order_assignment WHERE order_id = :orderId";
        return jdbc.query(sql, Map.of("orderId", orderId), mapper);
    }

    // 查询某个维修工的所有分配记录
    @Override
    public List<OrderAssignment> findByRepairmanId(Long repairmanId){
        String sql = "SELECT * FROM order_assignment WHERE repairman_id = :repairmanId";
        return jdbc.query(sql, Map.of("repairmanId", repairmanId), mapper);
    }
}
