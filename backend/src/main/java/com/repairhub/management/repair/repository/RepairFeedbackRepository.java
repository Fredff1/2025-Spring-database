package com.repairhub.management.repair.repository;

import java.util.List;
import java.util.Optional;

import com.repairhub.management.repair.entity.RepairFeedback;

public interface RepairFeedbackRepository {
    public int insert(RepairFeedback feedback);

    public int update(RepairFeedback feedback);

    public int delete(Long repairFeedbackId);

    public Optional<RepairFeedback> findById(Long repairFeedbackId);

    public List<RepairFeedback> findByRepairOrderId(Long repairOrderId);

    public List<RepairFeedback> findAll();

    public int insertAdminResponse(Long repairFeedbackId, String adminResponse);

}
