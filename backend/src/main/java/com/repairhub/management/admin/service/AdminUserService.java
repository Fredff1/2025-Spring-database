package com.repairhub.management.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.repairhub.management.auth.domain.enums.UserRole;
import com.repairhub.management.auth.entity.User;
import com.repairhub.management.auth.repository.UserRepository;
import com.repairhub.management.common.dto.PageResponse;
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

    public PageResponse<UserProfileDTO> findAllCustomers(int page,int limit){
        PageResponse<User> customers = userRepository.findAllByRoleWithPage(UserRole.CUSTOMER,page,limit);
        List<UserProfileDTO> dtos = customers.getList().stream()
        .map(customer -> userInfoService.getProfile(customer))
        .collect(Collectors.toList());
        return new PageResponse<>(dtos, customers.getTotal());
    }

    public PageResponse<RepairmanProfileDTO> findAllRepairmen(int page,int limit) {
        PageResponse<User> repairmen = userRepository.findAllByRoleWithPage(UserRole.REPAIRMAN,page,limit);
        List<RepairmanProfileDTO> dtos = repairmen.getList().stream()
            .map(repairman -> repairmanProfileService.getProfileDTO(repairman))
            .collect(Collectors.toList());
        return new PageResponse<>(dtos, repairmen.getTotal());
    }
}
