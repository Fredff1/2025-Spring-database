package com.repairhub.management.repair.repository;

import java.util.List;
import java.util.Optional;

import com.repairhub.management.repair.entity.LaborFeeLog;

public interface LaborFeeLogRepository {
    public int insert(LaborFeeLog log);

    public int update(LaborFeeLog log);

    public int delete(int id);

    public Optional<LaborFeeLog> findById(Long LaborFeeLogId);

    public List<LaborFeeLog> findAllByRepairOrderId(Long repairOrderId);

    public List<LaborFeeLog> findAllByRepairmanId(Long repairmanId);
}
