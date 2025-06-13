package com.repairhub.management.order.repository;

import java.util.List;
import java.util.Optional;

import com.repairhub.management.common.dto.PageResponse;
import com.repairhub.management.order.entity.OrderAssignment;
import com.repairhub.management.order.enums.AssignmentStatus;


public interface OrderAssignmentRepository {

    public int insert(OrderAssignment orderAssignment);

    public int update(OrderAssignment orderAssignment);

    public int deleteById(Long assignmentId);

    public Optional<OrderAssignment> findById(Long assignmentId);

    public List<OrderAssignment> findByOrderId(Long orderId);

    public List<OrderAssignment> findByRepairmanId(Long repairmanId);

    public List<OrderAssignment> findAll();

    public Boolean existByOrderIdAndStatus(Long orderId,AssignmentStatus status);

    public PageResponse<OrderAssignment> findAllWithPage(int pageNum,int pageSize);

    public PageResponse<OrderAssignment> findByRepairmanIdWithPage(Long repairmanId,int pageNum,int pageSize);
}
