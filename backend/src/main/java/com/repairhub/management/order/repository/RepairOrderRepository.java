package com.repairhub.management.order.repository;

import java.util.List;
import java.util.Optional;

import com.repairhub.management.order.entity.RepairOrder;

public interface RepairOrderRepository {
    public int insert(RepairOrder order);
    public int update(RepairOrder order);
    public int deleteById(Long orderId);
    public Optional<RepairOrder> findById(Long orderId);
    public List<RepairOrder> findByUserId(Long userId);
    public List<RepairOrder> findByUserIdAndVehicleId(Long userId, Long vehicleId);
    public int countByUserId(Long userId);
}
