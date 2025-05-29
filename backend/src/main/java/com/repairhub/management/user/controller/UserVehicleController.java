package com.repairhub.management.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.common.dto.PageResponse;
import com.repairhub.management.utils.PageUtils;
import com.repairhub.management.vehicle.controller.VehicleController;
import com.repairhub.management.vehicle.dto.VehicleDTO;
import com.repairhub.management.vehicle.entity.Vehicle;
import com.repairhub.management.vehicle.service.VehicleService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/user/vehicles")
public class UserVehicleController {
    private final VehicleService vehicleService;

    public UserVehicleController(
        VehicleService vehicleService
    ){
        this.vehicleService = vehicleService;
    }

    @GetMapping("")
    public ResponseEntity<PageResponse<VehicleDTO>> getVehicles(
        @RequestParam(defaultValue = "1")   int page,
        @RequestParam(defaultValue = "5")   int limit,
        @AuthenticationPrincipal User user
    ) {
        List<Vehicle> vehicles = vehicleService.getVehicles(user);
        List<Vehicle> pagedVehicles = PageUtils.paginate(vehicles, page, limit);
        List<VehicleDTO> dtos = pagedVehicles.stream().map(vehicle -> VehicleDTO.from(vehicle)).collect(Collectors.toList());
        var resp = new PageResponse<VehicleDTO>(dtos,vehicles.size());
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
    
}
