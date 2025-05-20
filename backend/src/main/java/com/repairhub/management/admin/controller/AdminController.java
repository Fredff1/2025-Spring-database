package com.repairhub.management.admin.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.repairhub.management.admin.dto.AdminInfoDTO;
import com.repairhub.management.admin.dto.OverviewDTO;
import com.repairhub.management.admin.dto.StatisticsDTO;
import com.repairhub.management.common.dto.CommonResponse;
import com.repairhub.management.order.dto.OrderDTO;

import io.swagger.v3.oas.models.responses.ApiResponse;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @GetMapping("/info")
    public CommonResponse<AdminInfoDTO> getInfo(){
        return null;
    }
    
    @GetMapping("/overview")
    public CommonResponse<OverviewDTO> getOverview(){
        return null;
    }
    
    @GetMapping("/pending-orders")
    public CommonResponse<List<OrderDTO>> getPendingOrders(){
        return null;
    }
    
    @GetMapping("/statistics")
    public CommonResponse<StatisticsDTO> getStatistics(@RequestParam String type){
        return null;
    }
}
