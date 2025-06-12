package com.repairhub.management.repair.repository;

import java.util.List;
import java.util.Optional;

import com.repairhub.management.common.dto.PageResponse;
import com.repairhub.management.repair.entity.LaborFeeLog;

public interface LaborFeeLogRepository {
    public int insert(LaborFeeLog log);

    public int update(LaborFeeLog log);

    public int delete(Long id);

    public Optional<LaborFeeLog> findById(Long LaborFeeLogId);

    public List<LaborFeeLog> findAllByRepairOrderId(Long repairOrderId);

    public List<LaborFeeLog> findAllByRepairmanId(Long repairmanId);

    public List<LaborFeeLog> findAll();

    public PageResponse<LaborFeeLog> findAllWithPage(int pageNum,int pageSize);
}
