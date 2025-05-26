package com.repairhub.management.order.repository;

import java.util.List;
import java.util.Optional;

import com.repairhub.management.order.entity.OrderAssignment;

public interface OrderAssignmentRepository {
    // 新增一条订单分配记录
    public int insert(OrderAssignment orderAssignment);

    // 更新订单分配记录（如接受或拒绝）
    public int update(OrderAssignment orderAssignment);

    // 删除订单分配记录
    public int deleteById(Long assignmentId);

    // 根据主键查找订单分配记录
    public Optional<OrderAssignment> findById(Long assignmentId);

    // 查询某个订单的所有分配记录
    public List<OrderAssignment> findByOrderId(Long orderId);

    // 查询某个维修工的所有分配记录
    public List<OrderAssignment> findByRepairmanId(Long repairmanId);
}
