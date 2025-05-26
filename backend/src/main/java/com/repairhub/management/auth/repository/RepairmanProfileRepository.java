package com.repairhub.management.auth.repository;

import java.util.List;

import com.repairhub.management.auth.entity.RepairmanProfile;

public interface RepairmanProfileRepository {

    public int insert(RepairmanProfile profile);
    public int update(RepairmanProfile profile);
    public List<RepairmanProfile> findAll();
    public List<RepairmanProfile> findByUserId(Long userId);
    public List<RepairmanProfile> findBySpecialty(String specialty);
}
