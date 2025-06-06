package com.repairhub.management.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.repairhub.management.admin.dto.AdminIncomeDTO;
import com.repairhub.management.admin.dto.AdminOverviewDTO;
import com.repairhub.management.admin.repository.AdminOverviewRepository;
import com.repairhub.management.auth.repository.UserRepository;
import com.repairhub.management.common.dto.PageResponse;
import com.repairhub.management.order.dto.OrderDTO;
import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.order.service.OrderService;
import com.repairhub.management.repair.dto.LaborFeeLogDTO;
import com.repairhub.management.repair.entity.LaborFeeLog;
import com.repairhub.management.repair.repository.LaborFeeLogRepository;
import com.repairhub.management.repairman.repository.RepairmanProfileRepository;
import com.repairhub.management.utils.PageUtils;
import com.repairhub.management.vehicle.dto.VehicleDTO;
import com.repairhub.management.vehicle.dto.VehicleDetailDTO;
import com.repairhub.management.vehicle.entity.Vehicle;
import com.repairhub.management.vehicle.repository.VehicleRepository;

@Service
public class AdminService {
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final LaborFeeLogRepository laborFeeLogRepository;
    private final RepairmanProfileRepository profileRepository;
    private final AdminOverviewRepository adminOverviewRepository;
    private final OrderService orderService;

    public AdminService(
        VehicleRepository vehicleRepository,
        UserRepository userRepository,
        LaborFeeLogRepository laborFeeLogRepository,
        RepairmanProfileRepository profileRepository,
        AdminOverviewRepository adminOverviewRepository,
        OrderService orderService
    ) {
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.laborFeeLogRepository = laborFeeLogRepository;
        this.profileRepository = profileRepository;
        this.adminOverviewRepository = adminOverviewRepository;
        this.orderService = orderService;
    }

    public PageResponse<VehicleDetailDTO> findAllVehicles(int pageNum,int pageSize) {
        PageResponse<Vehicle> vehicles = vehicleRepository.findAllWithPage(pageNum,pageSize);
        List<VehicleDetailDTO> dtos = vehicles.getList().stream()
        .map(vehicle -> VehicleDetailDTO.from(vehicle,userRepository.findById(vehicle.getOwnerId()).get()))
        .collect(Collectors.toList());
        return new PageResponse<>(dtos, vehicles.getTotal());
    }

    public AdminIncomeDTO getIncome(
        int page,
        int size
    ){
        PageResponse<LaborFeeLog> logs = laborFeeLogRepository.findAllWithPage(page,size);
        List<LaborFeeLog> pagedLogs = logs.getList();
        List<LaborFeeLogDTO> dtos = pagedLogs.stream()
        .map(log -> LaborFeeLogDTO.from(
            log, 
            profileRepository.findByUserId(log.getRepairmanId()).get(),
            userRepository.findById(log.getRepairmanId()).get()))
        .collect(Collectors.toList());
        AdminIncomeDTO incomeDTO = AdminIncomeDTO.builder()
        .list(dtos)
        .total(logs.getTotal())
        .build();
        return incomeDTO;
    }

    public List<OrderDTO> getUnfinishedOrders(Integer limit){
        List<RepairOrder> orders = adminOverviewRepository.findUnfinishedOrders(limit);
        List<OrderDTO> dtos = orders.stream().map(order -> orderService.toDTO(order)).collect(Collectors.toList());
        return dtos;
    }

    public AdminOverviewDTO getOverview(){
        AdminOverviewDTO dto = AdminOverviewDTO.builder()
        .todayOrders(adminOverviewRepository.countTodayOrders())
        .unfinishedOrders(adminOverviewRepository.countUnfinishedOrders())
        .activeRepairmen(adminOverviewRepository.countActiveRepairman())
        .monthlyCost(adminOverviewRepository.countMonthlyCost())
        .build();
        return dto;
    }
}
