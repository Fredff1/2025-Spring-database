package com.repairhub.management.admin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.repairhub.management.admin.service.AdminUserService;
import com.repairhub.management.common.dto.PageResponse;
import com.repairhub.management.repairman.dto.RepairmanProfileDTO;
import com.repairhub.management.user.dto.UserProfileDTO;
import com.repairhub.management.utils.PageUtils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/admin/account")
public class AdminUserController {

    private final AdminUserService adminUserService;;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }
    
    @GetMapping("/customers")
    public ResponseEntity<PageResponse<UserProfileDTO>> getAllCustomers(
        @RequestParam int page,
        @RequestParam int limit
    ) {
        var customers = adminUserService.findAllCustomers();
        PageResponse<UserProfileDTO> resp = new PageResponse<>(
            PageUtils.paginate(customers, page, limit), 
            customers.size());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/repairmen")
    public ResponseEntity<PageResponse<RepairmanProfileDTO>> getAllRepairmen(
        @RequestParam int page,
        @RequestParam int limit) {
        var repairmen = adminUserService.findAllRepairmen();
        PageResponse<RepairmanProfileDTO> resp = new PageResponse<>(
            PageUtils.paginate(repairmen, page, limit), 
            repairmen.size());
        return ResponseEntity.ok(resp);
    }

}
