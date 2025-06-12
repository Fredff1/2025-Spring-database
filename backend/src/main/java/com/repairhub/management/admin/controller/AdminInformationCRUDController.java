package com.repairhub.management.admin.controller;
import com.repairhub.management.vehicle.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.repairhub.management.admin.service.AdminService;
import com.repairhub.management.auth.domain.enums.UserStatus;
import com.repairhub.management.auth.dto.RegisterRequestDTO;
import com.repairhub.management.auth.dto.UserStatusDTO;
import com.repairhub.management.auth.entity.User;
import com.repairhub.management.auth.service.UserProfileService;
import com.repairhub.management.auth.service.UserService;
import com.repairhub.management.common.dto.CommonErrorResponse;
import com.repairhub.management.order.dto.CreateOrderRequest;
import com.repairhub.management.order.dto.UpdateOrderRequest;
import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.order.enums.OrderStatus;
import com.repairhub.management.order.service.OrderAssignmentService;
import com.repairhub.management.order.service.OrderService;
import com.repairhub.management.repair.dto.CreateFeedbackAdminResponseDTO;
import com.repairhub.management.repair.dto.CreateLaborFeeLogDTO;
import com.repairhub.management.repair.dto.CreateMaterialUsageDTO;
import com.repairhub.management.repair.dto.CreateRepairRecordDTO;
import com.repairhub.management.repair.service.RepairFeeService;
import com.repairhub.management.repair.service.RepairService;
import com.repairhub.management.repairman.dto.RepairmanCreateDTO;
import com.repairhub.management.repairman.dto.RepairmanProfileDTO;
import com.repairhub.management.repairman.dto.RepairmanProfileUpdateDTO;
import com.repairhub.management.repairman.entity.RepairmanProfile;
import com.repairhub.management.repairman.service.RepairmanProfileService;
import com.repairhub.management.user.service.UserInfoService;
import com.repairhub.management.vehicle.dto.CreateVehicleRequest;
import com.repairhub.management.vehicle.dto.UpdateVehicleRequest;

@RestController
@RequestMapping("/api/admin")
public class AdminInformationCRUDController {

    private final VehicleService vehicleService;
    private final AdminService adminService;
    private final UserInfoService userInfoService;
    private final OrderService orderService;
    private final OrderAssignmentService orderAssignmentService;
    private final UserService userService;
    private final UserProfileService userProfileService;
    private final RepairmanProfileService repairmanProfileService;
    private final RepairService repairService;
    private final RepairFeeService repairFeeService;

    public AdminInformationCRUDController(
        AdminService adminService,
        UserInfoService userInfoService,
        OrderService orderService,
        OrderAssignmentService orderAssignmentService,
        UserService userService,
        UserProfileService userProfileService,
        RepairmanProfileService repairmanProfileService,
        VehicleService vehicleService,
        RepairService repairService,
        RepairFeeService repairFeeService) {
        this.adminService = adminService;
        this.userInfoService = userInfoService;
        this.orderService = orderService;
        this.orderAssignmentService = orderAssignmentService;
        this.userService = userService;
        this.userProfileService = userProfileService;
        this.repairmanProfileService = repairmanProfileService;
        this.vehicleService = vehicleService;
        this.repairService = repairService;
        this.repairFeeService = repairFeeService;
    }

    //增量接口放在这

    /**
     * 创建用户顾客)
     * @param registerRequestDTO
     * @return
     */
    @PostMapping("/customers")
    public ResponseEntity<?> createUser(
        @RequestBody RegisterRequestDTO registerRequestDTO
    ) {
        try {
            userService.registerUser(registerRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new CommonErrorResponse(404, e.getMessage()));
        }
    }

    /**
     * 更新用户状态（包括顾客和维修人员）
     * @param userId
     * @param status
     * @return
     */
    @PostMapping("/users/status")
    public ResponseEntity<?> updateUserStatus(
        @RequestParam Long userId,
        @RequestParam String status
    ) {
        try {
            UserStatus userStatus = UserStatus.valueOf(status.toUpperCase());
            UserStatusDTO userStatusDTO = new UserStatusDTO();
            userStatusDTO.setStatus(userStatus);

            userProfileService.updateStatus(userId, userStatusDTO);       
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new CommonErrorResponse(404, e.getMessage()));
        }
    }

    /**
     * 删除用户（包括顾客和维修人员）
     * @param userId
     * @return
     */
    @DeleteMapping("/users")
    public ResponseEntity<?> deleteUser(
        @RequestParam Long userId
    ) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new CommonErrorResponse(404, e.getMessage()));
        }
    }

    /**
     * 创建维修工
     * @param registerRequestDTO
     * @return
     */
    @PostMapping("/repairmen")
    public ResponseEntity<?> createRepairman(
        @RequestBody RepairmanCreateDTO repairmanCreateDTO){
        try {

            RegisterRequestDTO registerRequestDTO = RegisterRequestDTO.builder()
            .username(repairmanCreateDTO.getUsername())
            .password(repairmanCreateDTO.getPassword())
            .confirmPassword(repairmanCreateDTO.getConfirmPassword())
            .email(repairmanCreateDTO.getEmail())
            .phone(repairmanCreateDTO.getPhone())
            .role(repairmanCreateDTO.getRole())
            .build();

            userService.registerUser(registerRequestDTO);

            RepairmanProfile repairmanProfile = RepairmanProfile.builder()
            .userId(userService.findUserIdByUsername(repairmanCreateDTO.getUsername()))
            .hourlyMoneyRate(repairmanCreateDTO.getHourlyMoneyRate())
            .specialty(repairmanCreateDTO.getSpecialty())
            .repairmanNumber(repairmanCreateDTO.getRepairmanNumber())
            .build();

            repairmanProfileService.insertProfile(repairmanProfile);


            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new CommonErrorResponse(404, e.getMessage()));
        }
    }

    /**
     * 更新维修工的个人信息
     * @param repairmanProfileDTO
     * @return
     */
    @PostMapping("/repairmen/profile")
    public ResponseEntity<?> updateRepairmanProfile(
        @RequestParam Long userId,
        RepairmanProfileUpdateDTO updateDTO) {
        try {
            repairmanProfileService.updateProfile(userId, updateDTO);
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new CommonErrorResponse(404, e.getMessage()));

        }
    }

    /**
     * 创建车辆
     * @param name
     * @param createVehicleRequest
     * @return
     */
    @PostMapping("/vehicles")
    public ResponseEntity<?> createVehicle(@RequestParam String name, 
                                            @RequestBody CreateVehicleRequest createVehicleRequest) {
        try {
            User user = userService.findUserByUsername(name);
            vehicleService.createVehicle(createVehicleRequest, user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new CommonErrorResponse(404, e.getMessage()));
        }

    }

    /**
     * 更新车辆信息
     * @param vehicleId
     * @param createVehicleRequest
     * @return
     */
    @PostMapping("/vehicles/update")
    public ResponseEntity<?> updateVehicle(
        @RequestParam Long vehicleId,
        @RequestBody UpdateVehicleRequest createVehicleRequest) {
        try {
            vehicleService.updateVehicle(createVehicleRequest,vehicleId);
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new CommonErrorResponse(404, e.getMessage()));
        }
    }

    /**
     * 删除车辆
     * @param vehicleId
     * @return
     */
    @DeleteMapping("/vehicles")
    public ResponseEntity<?> deleteVehicle(
        @RequestParam Long vehicleId
    ) {
        try {
            vehicleService.deleteVehicle(vehicleId);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new CommonErrorResponse(404, e.getMessage()));
        }
    }

    /**
     * 为指定用户创建维修订单
     * @param userName
     * @return
     */
    @PostMapping("/repair-order/create")
    public ResponseEntity<?> createRepairOrder(
        @RequestParam String userName,
        @RequestBody CreateOrderRequest createOrderRequest
    ){
        try {
            User user = userService.findUserByUsername(userName);
            orderService.createOrder(user,createOrderRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new CommonErrorResponse(404, e.getMessage()));
        }

    }

    /**
     * 更新维修订单状态
     * @param orderId
     * @return
     */
    @PostMapping("/repair-order/status")
    public ResponseEntity<?> updateRepairOrderStatus(
        @RequestParam Long orderId,
        @RequestBody UpdateOrderRequest request
    ) {
        try {
            orderService.updateRepairOrder(orderId,request);
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new CommonErrorResponse(404, e.getMessage()));
        }
    }

    /**
     * 分配指定的维修订单给指定的维修人员
     * @param orderId
     * @return
     */
    @PostMapping("/repair-order/assign")
    public ResponseEntity<?> assignRepairOrder(@RequestParam Long orderId,
                                               @RequestParam Long repairmanId) {
        try {
            RepairOrder repairOrder = orderService.findById(orderId)
                                                  .orElseThrow(() -> new IllegalArgumentException("RepairOrder not found for id: " + orderId));
            orderService.assignOrderToCertainRepairman(repairOrder, repairmanId);
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new CommonErrorResponse(404, e.getMessage()));
        }
    }

    @PostMapping("/repair-record/create/{repairmanId}")
    public ResponseEntity<?> createRepairRecord(
        @RequestBody CreateRepairRecordDTO createRepairRecordDTO,
        @RequestParam Long repairmanId
    ) {
        try {
            User repairman = userService.findUserById(repairmanId);
            repairService.submitRepairRecord(createRepairRecordDTO, repairman);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new CommonErrorResponse(404, e.getMessage()));
        }
    }

    @DeleteMapping("/repair-record/{id}")
    public ResponseEntity<?> deleteRepairRecord(
        @RequestParam Long id
    ) {
        try {
            repairService.deleteRepairRecord(id);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new CommonErrorResponse(404, e.getMessage()));
        }
    }

    /**
     * 新增某一个维修订单的材料使用情况
     * @param orderId
     * @param materialUsage
     * @return
     */
    @PostMapping("/material-usage/create")
    public ResponseEntity<?> createMaterialUsage(
        @RequestBody CreateMaterialUsageDTO materialUsage
    ) {
        try {
            repairService.submitMaterialUsage(materialUsage);
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new CommonErrorResponse(404, e.getMessage()));
        }
    }

    /**
     * 更新某一个维修订单的材料使用情况
     * @param materialUsageId
     * @param materialUsage
     * @return
     */
    @PostMapping("material-usage/update")
    public ResponseEntity<?> updateMaterialUsage(
        @RequestParam Long materialUsageId,
        @RequestBody CreateMaterialUsageDTO materialUsage
    ) {
        try {
            repairService.updateMaterialUsage(materialUsageId, materialUsage);
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new CommonErrorResponse(404, e.getMessage()));
        }
    }

    /**
     * 删除某一个维修订单的材料使用情况
     * @param materialUsageId
     * @return
     */
    @DeleteMapping("material-usage")
    public ResponseEntity<?> deleteMaterialUsage(
        @RequestParam Long materialUsageId
    ) {
        try {
            repairService.deleteMaterialUsage(materialUsageId);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new CommonErrorResponse(404, e.getMessage()));
        }
    }

    @PostMapping("/labor-fee-log/create")
    public ResponseEntity<?> createLaborFeeLog(
        @RequestBody CreateLaborFeeLogDTO createLaborFeeLogDTO
    ) {
        try {
            repairFeeService.createLaborFeeLog(createLaborFeeLogDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new CommonErrorResponse(404, e.getMessage()));
        }
    }

    @PostMapping("/labor-fee-log/{id}/update")
    public ResponseEntity<?> updateLaborFeeLog(
        @RequestBody CreateLaborFeeLogDTO updateDTO
        , @RequestParam Long id
    ) {
        try {
            repairFeeService.updateLaborFeeLog(id, updateDTO);
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new CommonErrorResponse(404, e.getMessage()));
        }
    }

    @DeleteMapping("/labor-fee-log/{id}")
    public ResponseEntity<?> deleteLaborFeeLog(
        @RequestParam Long id
    ) {
        try {
            repairFeeService.deleteLaborFeeLog(id);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new CommonErrorResponse(404, e.getMessage()));
        }
    }

    @PostMapping("/feedback/response")
    public ResponseEntity<?> insertAdminResponse(
        @RequestBody CreateFeedbackAdminResponseDTO feedbackAdminResponseDTO
    ) {
        try {
            repairService.insertAdminResponse(feedbackAdminResponseDTO);
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new CommonErrorResponse(404, e.getMessage()));
        }
    }

    @DeleteMapping("/feedback/{id}")
    public ResponseEntity<?> deleteFeedback(
        @RequestParam Long id
    ) {
        try {
            repairService.deleteFeedback(id);
            return ResponseEntity.ok(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new CommonErrorResponse(404, e.getMessage()));
        }
    }


}
