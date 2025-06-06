package com.repairhub.management.repairman.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.common.dto.CommonResponse;
import com.repairhub.management.common.dto.PageResponse;
import com.repairhub.management.order.entity.OrderAssignment;
import com.repairhub.management.order.repository.OrderAssignmentRepository;
import com.repairhub.management.order.service.OrderAssignmentService;
import com.repairhub.management.order.service.OrderService;
import com.repairhub.management.repair.dto.OrderAssignmentDTO;
import com.repairhub.management.utils.PageUtils;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/repairman/assignments")
public class RepairmanAssignmentController {
    private final OrderAssignmentService orderAssignmentService;
    private final OrderService orderService;

    public RepairmanAssignmentController(
        OrderAssignmentService orderAssignmentService,
        OrderService orderService
        ){
        this.orderAssignmentService = orderAssignmentService;
        this.orderService = orderService;
    }

    
    
    @GetMapping("")
    public ResponseEntity<PageResponse<OrderAssignmentDTO>> getAssignments(
        @RequestParam(defaultValue = "1")   int page,
        @RequestParam(defaultValue = "5")   int limit,
        @AuthenticationPrincipal User repairman
    ){
        PageResponse<OrderAssignment> assignments = orderAssignmentService.getRepairmanAssignments(repairman.getUserId(),page,limit);
        List<OrderAssignment> pageAssignments = assignments.getList();
        List<OrderAssignmentDTO> dtos = pageAssignments.stream()
        .map(assignment -> orderAssignmentService.convertAssignmentToDTO(assignment))
        .collect(Collectors.toList());
        PageResponse<OrderAssignmentDTO> resp = new PageResponse<>(dtos, assignments.getTotal());
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @PostMapping("/{assignmentId}/accept")
    public ResponseEntity<CommonResponse<Boolean>> acceptAssignment(
        @PathVariable Long assignmentId
        ) {
        orderAssignmentService.acceptAssignment(assignmentId);
        CommonResponse<Boolean> resp = new CommonResponse<>(200, "Assignment accepted", true);
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @PostMapping("/{assignmentId}/reject")
    public ResponseEntity<CommonResponse<Boolean>> rejectAssignment(
        @PathVariable Long assignmentId
        ) {
        orderAssignmentService.rejectAssignment(assignmentId);
        CommonResponse<Boolean> resp = new CommonResponse<>(200, "Assignment rejected", true);
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }
    
    

    
}
