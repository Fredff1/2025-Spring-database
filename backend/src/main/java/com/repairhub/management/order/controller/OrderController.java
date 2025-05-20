package com.repairhub.management.order.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.repairhub.management.common.dto.CommonResponse;
import com.repairhub.management.common.dto.PageRequest;
import com.repairhub.management.common.dto.PageResponse;
import com.repairhub.management.order.dto.CompleteRepairRequest;
import com.repairhub.management.order.dto.CreateOrderRequest;
import com.repairhub.management.order.dto.OrderDTO;
import com.repairhub.management.order.dto.OrderStatsDTO;
import com.repairhub.management.order.dto.ReviewRequest;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @GetMapping
    public ResponseEntity<PageResponse<OrderDTO>> getList(PageRequest request){
        OrderDTO order = OrderDTO.toDefault();
        List<OrderDTO> resultList = new ArrayList<>();
        resultList.add(order);
        PageResponse<OrderDTO> pageResponse = PageResponse.<OrderDTO>builder()
        .list(resultList)
        .total(10L).build();
        return new ResponseEntity<>(pageResponse,HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getDetail(@PathVariable Long id){
        OrderDTO order = OrderDTO.toDefault();
       return new ResponseEntity<>(order,HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<OrderDTO> create(@RequestBody CreateOrderRequest request){
        return null;
    }
    
    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderDTO> cancel(@PathVariable Long id){
        return null;
    }
    
    @PostMapping("/{id}/pay")
    public ResponseEntity<OrderDTO> pay(@PathVariable Long id){
        return null;
    }
    
    @PostMapping("/{id}/review")
    public ResponseEntity<OrderDTO> review(@PathVariable Long id, @RequestBody ReviewRequest request){
        return null;
    }
    
    @GetMapping("/stats")
    public ResponseEntity<OrderStatsDTO> getStats(){
        OrderStatsDTO statsDTO = OrderStatsDTO.builder()
        .historyCount(1)
        .orderCount(1)
        .vehicleCount(1)
        .build();
        return new ResponseEntity<>(statsDTO,HttpStatus.OK);
    }
    
    @PostMapping("/{id}/start-repair")
    public ResponseEntity<OrderDTO> startRepair(@PathVariable Long id){
        return null;
    }
    
    @PostMapping("/{id}/complete-repair")
    public ResponseEntity<OrderDTO> completeRepair(@PathVariable Long id, @RequestBody CompleteRepairRequest request){
        return null;
    }
    
    @PostMapping("/{id}/assign")
    public ResponseEntity<OrderDTO> assignRepairman(@PathVariable Long id, @RequestParam Long repairmanId){
        return null;
    }
    
    @PostMapping("/{id}/complete")
    public ResponseEntity<OrderDTO> complete(@PathVariable Long id){
        return null;
    }
}
