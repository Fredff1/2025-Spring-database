package com.repairhub.management.repairman.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.common.dto.CommonResponse;
import com.repairhub.management.common.dto.PageResponse;
import com.repairhub.management.order.dto.OrderDTO;
import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.order.service.OrderService;
import com.repairhub.management.repair.dto.CreateMaterialUsageDTO;
import com.repairhub.management.repair.dto.CreateRepairRecordDTO;
import com.repairhub.management.repair.dto.MaterialUsageDTO;
import com.repairhub.management.repair.dto.RepairFeedbackDTO;
import com.repairhub.management.repair.dto.RepairRecordDTO;
import com.repairhub.management.repair.entity.MaterialUsage;
import com.repairhub.management.repair.entity.RepairFeedback;
import com.repairhub.management.repair.entity.RepairRecord;
import com.repairhub.management.repair.service.RepairService;
import com.repairhub.management.repairman.service.RepairmanProfileService;
import com.repairhub.management.utils.PageUtils;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/repairman/repair-orders")
public class RepairmanOrderController {


    private final RepairService repairService;
    private final RepairmanProfileService repairmanProfileService;
    private final OrderService orderService;
    

    public RepairmanOrderController(
        RepairService repairService,
        RepairmanProfileService repairmanProfileService,
        OrderService orderService
    ){
        this.repairService = repairService;
        this.repairmanProfileService = repairmanProfileService;
        this.orderService = orderService;
    }

    @GetMapping("")
    public ResponseEntity<PageResponse<OrderDTO>> getOrders(
        @RequestParam(defaultValue = "1")   int page,
        @RequestParam(defaultValue = "5")   int limit,
        @AuthenticationPrincipal User repairman
    ) {
        PageResponse<RepairOrder> orders = repairmanProfileService.findRepairmanOrders(repairman,page,limit);
        List<RepairOrder> pagedOrders = orders.getList();
        List<OrderDTO> dtos = pagedOrders.stream()
        .map(order -> orderService.toDTO(order))
        .collect(Collectors.toList());
        PageResponse<OrderDTO> resp = new PageResponse<>(dtos, orders.getTotal());
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(
        @PathVariable Long orderId
    ) {
        RepairOrder order = orderService.findById(orderId).get();
        OrderDTO dto = orderService.toDTO(order);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
    
    @GetMapping("/{orderId}/records")
    public ResponseEntity<List<RepairRecordDTO>> getRepairRecords(
        @PathVariable("orderId") Long orderId
    ) {
        List<RepairRecord> records = repairService.getRepairRecords(orderId);
        List<RepairRecordDTO> dtos = records.stream()
        .map(record -> repairService.convertRecordToDTO(record))
        .collect(Collectors.toList());
        return new ResponseEntity<>(dtos,HttpStatus.OK);
    }

    @PostMapping("/records")
    public ResponseEntity<CommonResponse<Boolean>> submitRepairRecord(
        @RequestBody @Valid CreateRepairRecordDTO request,
        @AuthenticationPrincipal User repairman
        ) {
        repairService.submitRepairRecord(request,repairman);
        CommonResponse<Boolean> resp = new CommonResponse<Boolean>(201, "Submit success", true);
        return new ResponseEntity<>(resp,HttpStatus.CREATED);
    }
    

    @GetMapping("/{orderId}/materials")
    public ResponseEntity<List<MaterialUsageDTO>> getRepairMaterials(
        @PathVariable("orderId") Long orderId
    ) { 
        List<MaterialUsage> materialUsages = repairService.getMaterialUsages(orderId);
        List<MaterialUsageDTO> dtos = materialUsages.stream()
        .map(materialUsage -> repairService.convertMaterialUsageToDTO(materialUsage))
        .collect(Collectors.toList());
        return new ResponseEntity<>(dtos,HttpStatus.OK);
    }

    @GetMapping("/{orderId}/feedback")
    public ResponseEntity<List<RepairFeedbackDTO>> getFeedbackList(
        @PathVariable("orderId") Long orderId
    ) {
        List<RepairFeedback> feedbacks = repairService.getRepairFeedbacks(orderId);
        List<RepairFeedbackDTO> dtos = feedbacks.stream()
        .map(feedback -> repairService.convertFeedbackToDTO(feedback))
        .collect(Collectors.toList());
        return new ResponseEntity<>(dtos,HttpStatus.OK);
    }
}
