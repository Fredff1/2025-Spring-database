package com.repairhub.management.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.repairhub.management.vehicle.controller.VehicleController;
import com.repairhub.management.vehicle.service.VehicleService;
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
    public String getVehicles(@RequestParam String param) {
        return new String();
    }
    
}
