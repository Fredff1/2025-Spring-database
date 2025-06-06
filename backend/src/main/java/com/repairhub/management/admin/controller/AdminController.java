package com.repairhub.management.admin.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.repairhub.management.admin.dto.AdminIncomeDTO;
import com.repairhub.management.admin.dto.AdminInfoDTO;
import com.repairhub.management.admin.dto.AdminSystemStatusDTO;
import com.repairhub.management.admin.dto.AdminOverviewDTO;
import com.repairhub.management.admin.dto.StatisticsDTO;
import com.repairhub.management.admin.service.AdminService;
import com.repairhub.management.auth.entity.User;
import com.repairhub.management.common.dto.CommonResponse;
import com.repairhub.management.common.dto.PageResponse;
import com.repairhub.management.order.dto.OrderDTO;
import com.repairhub.management.order.entity.OrderAssignment;
import com.repairhub.management.order.service.OrderAssignmentService;
import com.repairhub.management.repair.dto.OrderAssignmentDTO;
import com.repairhub.management.repairman.dto.RepairmanIncomeDTO;
import com.repairhub.management.user.dto.UserProfileDTO;
import com.repairhub.management.user.service.UserInfoService;
import com.repairhub.management.utils.PageUtils;
import com.repairhub.management.vehicle.dto.VehicleDTO;
import com.repairhub.management.vehicle.dto.VehicleDetailDTO;

import io.swagger.v3.oas.models.responses.ApiResponse;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserInfoService userInfoService;
    private final OrderAssignmentService orderAssignmentService;

    public AdminController(
        AdminService adminService,
        UserInfoService userInfoService,
        OrderAssignmentService orderAssignmentService) {
        this.adminService = adminService;
        this.userInfoService = userInfoService;
        this.orderAssignmentService = orderAssignmentService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getProfile(
        @AuthenticationPrincipal User user
    ) {
        var dto = userInfoService.getProfile(user);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
    

    @GetMapping("/vehicles")
    public ResponseEntity<PageResponse<VehicleDetailDTO>> getVehicles(
        @RequestParam int page,
        @RequestParam int limit
    ) {
        List<VehicleDetailDTO> dtos = adminService.findAllVehicles();
        PageResponse<VehicleDetailDTO> response = new PageResponse<>(
        PageUtils.paginate(dtos, page, limit),
        dtos.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/assignments")
    public ResponseEntity<PageResponse<OrderAssignmentDTO>> getAssignments(
        @RequestParam(defaultValue = "1")   int page,
        @RequestParam(defaultValue = "5")   int limit
    ){
        List<OrderAssignment> assignments = orderAssignmentService.getAllAssignments();
        List<OrderAssignment> pageAssignments = PageUtils.paginate(assignments, page, limit);
        List<OrderAssignmentDTO> dtos = pageAssignments.stream()
        .map(assignment -> orderAssignmentService.convertAssignmentToDTO(assignment))
        .collect(Collectors.toList());
        PageResponse<OrderAssignmentDTO> resp = new PageResponse<>(dtos, assignments.size());
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @GetMapping("/income")
    public ResponseEntity<AdminIncomeDTO> getIncome(
        @RequestParam int page,
        @RequestParam int size
    ) {
        AdminIncomeDTO incomeDTO = adminService.getIncome(page, size);
        return ResponseEntity.ok(incomeDTO);
    }

    @GetMapping("/overview")
    public ResponseEntity<AdminOverviewDTO> getOverview() {
        AdminOverviewDTO dto = adminService.getOverview();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/system/status")
    public ResponseEntity<AdminSystemStatusDTO> getSystemStatus() {
        var dto = AdminSystemStatusDTO.toDefault();
        return ResponseEntity.ok(dto);
    }
    
    
    


}
