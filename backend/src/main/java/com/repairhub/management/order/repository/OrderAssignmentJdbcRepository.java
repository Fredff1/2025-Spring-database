package com.repairhub.management.order.repository;

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

import com.repairhub.management.order.entity.OrderAssignment;
import com.repairhub.management.order.entity.OrderAssignmentRowMapper;

@Repository
public class OrderAssignmentJdbcRepository implements OrderAssignmentRepository{
    
    private final NamedParameterJdbcTemplate jdbc;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final OrderAssignmentRowMapper mapper = new OrderAssignmentRowMapper();

    public OrderAssignmentJdbcRepository(
        NamedParameterJdbcTemplate jdbc,
        DataSource dataSource
    ) {
        this.jdbc = jdbc;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("assignment")
            .usingGeneratedKeyColumns("assignment_id");
    }

    // 新增一条订单分配记录
    @Override
    public int insert(OrderAssignment orderAssignment){
        SqlParameterSource params = new MapSqlParameterSource()
        .addValue("order_id", orderAssignment.getOrderId())
        .addValue("repairman_id", orderAssignment.getRepairmanId())
        .addValue("assignment_time", orderAssignment.getAssignmentTime())
        .addValue("assignment_status", orderAssignment.getStatus().name())
        .addValue("actual_work_hours", orderAssignment.getActualWorkHour());
        Number key = simpleJdbcInsert.executeAndReturnKey(params);
        long assignmentId = key.longValue();
        orderAssignment.setAssignmentId(assignmentId);
        return 1;
    }

    // 更新订单分配记录（如接受或拒绝）
    @Override
    public int update(OrderAssignment orderAssignment){
        String sql = """
        UPDATE assignment
        SET order_id           = :orderId,
            repairman_id       = :repairmanId,
            assignment_time    = :assignmentTime,
            assignment_status  = :status,
            actual_work_hours  = :actualWorkHours
        WHERE assignment_id      = :assignmentId
        """;

        SqlParameterSource params = new MapSqlParameterSource()
            .addValue("orderId",           orderAssignment.getOrderId())
            .addValue("repairmanId",       orderAssignment.getRepairmanId())
            .addValue("assignmentTime",    orderAssignment.getAssignmentTime())
            .addValue("status",            orderAssignment.getStatus().name())
            .addValue("actualWorkHours",   orderAssignment.getActualWorkHour())
            .addValue("assignmentId",      orderAssignment.getAssignmentId());

        return jdbc.update(sql, params);
    }

    // 删除订单分配记录
    @Override
    public int deleteById(Long assignmentId){
        String sql = "DELETE FROM assignment WHERE assignment_id = :assignmentId";
        return jdbc.update(sql, Map.of("assignmentId", assignmentId));
    }

    // 根据主键查找订单分配记录
    @Override
    public Optional<OrderAssignment> findById(Long assignmentId){
        String sql = "SELECT * FROM assignment WHERE assignment_id = :assignmentId";
        return jdbc.query(sql, Map.of("assignmentId", assignmentId), mapper)
                   .stream().findFirst();
    }

    // 查询某个订单的所有分配记录
    @Override
    public List<OrderAssignment> findByOrderId(Long orderId){
        String sql = "SELECT * FROM assignment WHERE order_id = :orderId";
        return jdbc.query(sql, Map.of("orderId", orderId), mapper);
    }

    // 查询某个维修工的所有分配记录
    @Override
    public List<OrderAssignment> findByRepairmanId(Long repairmanId){
        String sql = "SELECT * FROM assignment WHERE repairman_id = :repairmanId";
        return jdbc.query(sql, Map.of("repairmanId", repairmanId), mapper);
    }
}
