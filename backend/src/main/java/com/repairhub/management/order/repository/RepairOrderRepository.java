package com.repairhub.management.order.repository;

import java.util.List;
import java.util.Optional;

import com.repairhub.management.common.dto.PageResponse;
import com.repairhub.management.order.entity.RepairOrder;

public interface RepairOrderRepository {
    public int insert(RepairOrder order);
    public int update(RepairOrder order);
    public int deleteById(Long orderId);
    public Optional<RepairOrder> findById(Long orderId);
    public List<RepairOrder> findByUserId(Long userId);
    public List<RepairOrder> findByUserIdAndVehicleId(Long userId, Long vehicleId);
    public int countByUserId(Long userId);
    public List<RepairOrder> findAll();
    public PageResponse<RepairOrder> findAllWithPage(int pageNum,int pageSize);
    public PageResponse<RepairOrder> findByUserIdWithPage(Long userId,int pageNum,int pageSize);
    public PageResponse<RepairOrder> findByRepairmanIdWithPage(Long repairmanId,int pageNum,int pageSize);
}
