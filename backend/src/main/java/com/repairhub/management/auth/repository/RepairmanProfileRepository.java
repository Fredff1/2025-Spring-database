package com.repairhub.management.auth.repository;

import java.util.List;
import java.util.Optional;

import com.repairhub.management.auth.entity.RepairmanProfile;
import com.repairhub.management.repair.enums.FaultType;

public interface RepairmanProfileRepository {

    public int insert(RepairmanProfile profile);
    public int update(RepairmanProfile profile);
    public List<RepairmanProfile> findAll();
    public Optional<RepairmanProfile> findByUserId(Long userId);
    public List<RepairmanProfile> findBySpecialty(FaultType specialty);
}
