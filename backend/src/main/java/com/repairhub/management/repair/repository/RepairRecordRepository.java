package com.repairhub.management.repair.repository;

import java.util.List;
import java.util.Optional;

import com.repairhub.management.repair.entity.RepairRecord;

public interface RepairRecordRepository {
    public int insert(RepairRecord repairRecord);

    public int update(RepairRecord repairRecord);

    public int delete(Long repairRecordId);

    public Optional<RepairRecord> findById(Long repairRecordId);

    public List<RepairRecord> findByRepairOrderId(Long repairOrderId);

    public List<RepairRecord> findByRepairmanId(Long repairmanId);

    public List<RepairRecord> findAll();

    public int countByUserId(Long userId);
}   

