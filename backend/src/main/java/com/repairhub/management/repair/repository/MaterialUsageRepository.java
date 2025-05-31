package com.repairhub.management.repair.repository;

import java.util.List;
import java.util.Optional;

import com.repairhub.management.repair.entity.MaterialUsage;

public interface MaterialUsageRepository {
    public int insert(MaterialUsage materialUsage);

    public int update(MaterialUsage materialUsage);

    public int delete(int materialId);

    public Optional<MaterialUsage> findById(Long materialId);

    public List<MaterialUsage> findByRepairOrderId(Long repairOrderId);

    public List<MaterialUsage> findByRepairmanId(Long repairmanId);

    public List<MaterialUsage> findAll();
}
