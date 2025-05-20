package com.repairhub.management.vehicle.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.repairhub.management.common.dto.CommonResponse;
import com.repairhub.management.common.dto.PageRequest;
import com.repairhub.management.common.dto.PageResponse;
import com.repairhub.management.order.dto.OrderDTO;
import com.repairhub.management.vehicle.dto.CreateVehicleRequest;
import com.repairhub.management.vehicle.dto.RepairHistoryDTO;
import com.repairhub.management.vehicle.dto.UpdateVehicleRequest;
import com.repairhub.management.vehicle.dto.VehicleDTO;



@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    @GetMapping
    public ResponseEntity<PageResponse<VehicleDTO>> getList(PageRequest request){
        VehicleDTO vehicleDTO = VehicleDTO.builder()
        .createTime(LocalDateTime.now())
        .model("model-x")
        .plateNumber("plate-123456")
        .updateTime(LocalDateTime.now())
        .brand("HAH")
        .year(2024)
        .vin("vin-default")
        .build();
        List<VehicleDTO> resultList = new ArrayList<>();
        resultList.add(vehicleDTO);
        PageResponse<VehicleDTO> pageResponse = PageResponse.<VehicleDTO>builder()
        .list(resultList)
        .total(10L).build();
        return new ResponseEntity<>(pageResponse,HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getDetail(@PathVariable Long id){
        return null;
    }
    
    @PostMapping
    public ResponseEntity<VehicleDTO> create(@RequestBody CreateVehicleRequest request){
        return null;
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<VehicleDTO> update(@PathVariable Long id, @RequestBody UpdateVehicleRequest request){
        return null;
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id){
        return null;
    }
    
    @GetMapping("/{id}/repair-history")
    public ResponseEntity<List<RepairHistoryDTO>> getRepairHistory(@PathVariable Long id){
        return null;
    }
}
