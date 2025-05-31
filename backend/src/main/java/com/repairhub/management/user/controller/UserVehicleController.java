package com.repairhub.management.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.common.dto.CommonResponse;
import com.repairhub.management.common.dto.PageResponse;
import com.repairhub.management.utils.PageUtils;
import com.repairhub.management.vehicle.controller.VehicleController;
import com.repairhub.management.vehicle.dto.CreateVehicleRequest;
import com.repairhub.management.vehicle.dto.UpdateVehicleRequest;
import com.repairhub.management.vehicle.dto.VehicleDTO;
import com.repairhub.management.vehicle.entity.Vehicle;
import com.repairhub.management.vehicle.service.VehicleService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



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

    @PostMapping("")
    public ResponseEntity<CommonResponse<Boolean>> createVehicle(
        @RequestBody CreateVehicleRequest request,
        @AuthenticationPrincipal User user
        ) {
        vehicleService.createVehicle(request, user);
        CommonResponse<Boolean> response = CommonResponse.<Boolean>builder()
            .code(201)
            .message("Vehicle created successfully")
            .data(true)
            .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{vehicleId}")
    public ResponseEntity<CommonResponse<Boolean>> postMethodName(
        @PathVariable Long vehicleId,
        @RequestBody UpdateVehicleRequest request
    ) {
        vehicleService.updateVehicle(request, vehicleId);
        CommonResponse<Boolean> response = CommonResponse.<Boolean>builder()
            .code(200)
            .message("Vehicle updated successfully")
            .data(true)
            .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    
    }
    
    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<CommonResponse<Boolean>> deleteVehicle(
        @PathVariable Long vehicleId,
        @AuthenticationPrincipal User user
    ) {
        vehicleService.deleteVehicle(vehicleId, user);
        CommonResponse<Boolean> response = CommonResponse.<Boolean>builder()
            .code(200)
            .message("Vehicle deleted successfully")
            .data(true)
            .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    
    
}
