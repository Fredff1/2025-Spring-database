package com.repairhub.management.user.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.common.dto.CommonResponse;
import com.repairhub.management.common.dto.PageResponse;
import com.repairhub.management.order.dto.CreateOrderRequest;
import com.repairhub.management.order.dto.OrderDTO;
import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.order.repository.RepairOrderRepository;
import com.repairhub.management.order.service.OrderService;
import com.repairhub.management.repair.dto.MaterialUsageDTO;
import com.repairhub.management.repair.dto.RepairFeedbackDTO;
import com.repairhub.management.repair.dto.RepairRecordDTO;
import com.repairhub.management.repair.entity.RepairFeedback;
import com.repairhub.management.repair.entity.RepairRecord;
import com.repairhub.management.repair.service.RepairService;
import com.repairhub.management.utils.PageUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/user/repair-orders")
public class UserOrderController {

    private final OrderService orderService;
    private final RepairService repairService;
    

    public UserOrderController(
        OrderService orderService,
        RepairService repairService
    ){
        this.orderService = orderService;
        this.repairService = repairService;
    }
    

    @GetMapping()
    public ResponseEntity<PageResponse<OrderDTO>> getOrders(
        @RequestParam(defaultValue = "1")   int page,
        @RequestParam(defaultValue = "5")   int limit,
        @AuthenticationPrincipal User user
    ) {
        List<RepairOrder> orders = orderService.getOrders(user);
        var pagedOrders = PageUtils.paginate(orders, page, limit);
        List<OrderDTO> dtos = pagedOrders.stream().map(order -> orderService.toDTO(order)).collect(Collectors.toList());
        PageResponse<OrderDTO> resp = new PageResponse<OrderDTO>(dtos, orders.size());
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<CommonResponse<String>> createOrder(
        @RequestBody CreateOrderRequest request,
        @AuthenticationPrincipal User user) {
        //TODO: process POST request
        orderService.createOrder(user, request);
        var resp = CommonResponse.toResponse(201, "Success", "Creation success");
         return new ResponseEntity<>(resp,HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}/records")
    public ResponseEntity<List<RepairRecordDTO>> getRepairRecords(
        @PathVariable("orderId") Long orderId,
        @AuthenticationPrincipal User user
    ) {
        List<RepairRecord> records = repairService.getRepairRecords(orderId);
        List<RepairRecordDTO> dtos = records.stream()
        .map(record -> repairService.convertRecordToDTO(record))
        .collect(Collectors.toList());
        return new ResponseEntity<>(dtos,HttpStatus.OK);
    }

    @GetMapping("/{orderId}/materials")
    public ResponseEntity<List<MaterialUsageDTO>> getRepairMaterials(
        @PathVariable("orderId") Long orderId,
        @AuthenticationPrincipal User user
    ) {
        List<MaterialUsageDTO> list = List.of();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @GetMapping("/{orderId}/feedback")
    public ResponseEntity<List<RepairFeedbackDTO>> getFeedbackList(
        @PathVariable("orderId") Long orderId,
        @AuthenticationPrincipal User user
    ) {
        List<RepairFeedback> feedbacks = repairService.getRepairFeedbacks(orderId);
        List<RepairFeedbackDTO> dtos = feedbacks.stream()
        .map(feedback -> repairService.convertFeedbackToDTO(feedback))
        .collect(Collectors.toList());
        return new ResponseEntity<>(dtos,HttpStatus.OK);
    }
    
    
}
