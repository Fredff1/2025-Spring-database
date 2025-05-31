package com.repairhub.management.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.repairhub.management.auth.domain.enums.UserRole;
import com.repairhub.management.auth.entity.User;
import com.repairhub.management.auth.repository.UserRepository;
import com.repairhub.management.repairman.dto.RepairmanProfileDTO;
import com.repairhub.management.repairman.repository.RepairmanProfileRepository;
import com.repairhub.management.repairman.service.RepairmanProfileService;
import com.repairhub.management.user.dto.UserProfileDTO;
import com.repairhub.management.user.service.UserInfoService;

@Service
public class AdminUserService {
    private final UserRepository userRepository;
    private final RepairmanProfileRepository profileRepository;
    private final UserInfoService userInfoService;
    private final RepairmanProfileService repairmanProfileService;

    public AdminUserService(
        UserRepository userRepository,
        RepairmanProfileRepository profileRepository,
        UserInfoService userInfoService,
        RepairmanProfileService repairmanProfileService
    ) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.userInfoService = userInfoService;
        this.repairmanProfileService = repairmanProfileService;
    }

    public List<UserProfileDTO> findAllCustomers(){
        List<User> customers = userRepository.findAllByRole(UserRole.CUSTOMER);
        List<UserProfileDTO> dtos = customers.stream()
        .map(customer -> userInfoService.getProfile(customer))
        .collect(Collectors.toList());
        return dtos;
    }

    public List<RepairmanProfileDTO> findAllRepairmen() {
        List<User> repairmen = userRepository.findAllByRole(UserRole.REPAIRMAN);
        List<RepairmanProfileDTO> dtos = repairmen.stream()
            .map(repairman -> repairmanProfileService.getProfileDTO(repairman))
            .collect(Collectors.toList());
        return dtos;
    }
}
