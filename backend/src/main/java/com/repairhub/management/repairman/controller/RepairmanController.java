package com.repairhub.management.repairman.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.repairhub.management.repairman.repository.RepairmanProfileRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/repairman")
public class RepairmanController {
    
    private final RepairmanProfileRepository repairmanProfileRepository;

    public RepairmanController(
        RepairmanProfileRepository repairmanProfileRepository
    ){
        this.repairmanProfileRepository = repairmanProfileRepository;
    }

    @GetMapping("/profile")
    public String getProfile(@RequestParam String param) {
        return new String();
    }
    
}
