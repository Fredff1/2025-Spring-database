package com.repairhub.management.repairman.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.common.dto.CommonResponse;
import com.repairhub.management.repairman.dto.RepairmanIncomeDTO;
import com.repairhub.management.repairman.dto.RepairmanOverviewDTO;
import com.repairhub.management.repairman.dto.RepairmanProfileDTO;
import com.repairhub.management.repairman.dto.RepairmanProfileUpdateDTO;
import com.repairhub.management.repairman.dto.RepairmanStatisticDTO;
import com.repairhub.management.repairman.repository.RepairmanProfileRepository;
import com.repairhub.management.repairman.service.RepairmanProfileService;
import com.repairhub.management.utils.JwtUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/repairman")
public class RepairmanController {
    
    private final RepairmanProfileService profileService;
    private final JwtUtil jwtUtil;

    public RepairmanController(
        RepairmanProfileService profileService,
        JwtUtil jwtUtil
    ){
        this.profileService = profileService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/profile")
    public ResponseEntity<RepairmanProfileDTO> getProfile(
        @AuthenticationPrincipal User repairman) {
        RepairmanProfileDTO dto = profileService.getProfileDTO(repairman);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/profile")
    public ResponseEntity<Map<String, String>> updateProfile(@AuthenticationPrincipal User user,
                                @RequestBody RepairmanProfileUpdateDTO updateDTO) {
        var updatedProfile = profileService.updateProfile(user.getUserId(), updateDTO);
        String newToken = jwtUtil.generateToken(updatedProfile.getUsername());

        return new ResponseEntity<>(Map.of("token", newToken), HttpStatus.OK);
    }
    

    @GetMapping("/statistics")
    public ResponseEntity<RepairmanStatisticDTO> getStatistics(
        @RequestParam(required = false) LocalDateTime startTime,
        @AuthenticationPrincipal User repairman) {
        RepairmanStatisticDTO dto = profileService.getStatistics(repairman, startTime);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/overview")
    public ResponseEntity<RepairmanOverviewDTO> getOverview(
        @AuthenticationPrincipal User repairman) {
        RepairmanOverviewDTO dto = profileService.getOverview(repairman);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/income")
    public ResponseEntity<RepairmanIncomeDTO> getIncome(
        @RequestParam int page,
        @RequestParam int size,
        @RequestParam LocalDate startDate,
        @RequestParam LocalDate endDate,
        @AuthenticationPrincipal User repairman
    ) {
        RepairmanIncomeDTO incomeDTO = profileService.getIncomeStatistics(repairman, page, size, startDate, endDate);
        return ResponseEntity.ok(incomeDTO);
    }
    
    
}
