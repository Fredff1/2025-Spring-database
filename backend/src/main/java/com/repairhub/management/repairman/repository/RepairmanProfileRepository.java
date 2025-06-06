package com.repairhub.management.repairman.repository;

import java.util.List;
import java.util.Optional;

import com.repairhub.management.repair.enums.FaultType;
import com.repairhub.management.repairman.dto.RepairmanProfileUpdateDTO;
import com.repairhub.management.repairman.entity.RepairmanProfile;

public interface RepairmanProfileRepository {

    public int insert(RepairmanProfile profile);
    public int update(Long userId,RepairmanProfileUpdateDTO profile);
    public List<RepairmanProfile> findAll();
    public Optional<RepairmanProfile> findByUserId(Long userId);
    public List<RepairmanProfile> findBySpecialty(FaultType specialty);
    
}
