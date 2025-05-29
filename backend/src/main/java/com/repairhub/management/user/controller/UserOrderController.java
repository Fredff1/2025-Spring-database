package com.repairhub.management.user.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.repairhub.management.utils.PageUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/user/orders")
public class UserOrderController {

    private final OrderService orderService;
    

    public UserOrderController(
        OrderService orderService
    ){
        this.orderService = orderService;
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
    
}
