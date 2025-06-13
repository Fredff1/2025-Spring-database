package com.repairhub.management.admin.controller;
import com.repairhub.management.vehicle.service.VehicleService;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.repairhub.management.order.enums.AssignmentStatus;
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
    @PostMapping("/users/update-status/{id}")
    public ResponseEntity<?> updateUserStatus(
        @PathVariable("id") Long userId,
        @RequestBody UserStatusDTO userStatusDTO
    ) {
        try {
            userProfileService.updateStatus(userId, userStatusDTO);       
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new CommonErrorResponse(404, e.getMessage()));
        }
    }

    /**
     * 删除用户（包括顾客和维修人员）
     * @param id
     * @return
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(
        @PathVariable Long id
    ) {
        try {
            userService.deleteUser(id);
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

            User newRepair = userService.registerUser(registerRequestDTO);

            RepairmanProfile repairmanProfile = RepairmanProfile.builder()
            .userId(newRepair.getUserId())
            .hourlyMoneyRate(repairmanCreateDTO.getHourlyMoneyRate())
            .specialty(repairmanCreateDTO.getSpecialty())
            .repairmanNumber(generateRepairmanNumber())
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
    @PostMapping("/repairmen/profile/{id}")
    public ResponseEntity<?> updateRepairmanProfile(
        @PathVariable Long id,
        @RequestBody RepairmanProfileUpdateDTO updateDTO) {
        try {
            //输出dto
            
            repairmanProfileService.updateSpecialtyAndRate(id, updateDTO);
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
    @PostMapping("/vehicles/{name}/create")
    public ResponseEntity<?> createVehicle(@PathVariable String name, 
                                            @RequestBody CreateVehicleRequest createVehicleRequest) {
        try {
            User user = userService.findUserByUsername(name);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body(new CommonErrorResponse(404, "User not found"));
            }
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
    @PostMapping("/vehicles/update/{id}")
    public ResponseEntity<?> updateVehicle(
        @PathVariable Long id,
        @RequestBody UpdateVehicleRequest updateVehicleRequest) {
        try {
            vehicleService.updateVehicle(updateVehicleRequest,id);
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
    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity<?> deleteVehicle(
        @PathVariable Long id
    ) {
        try {
            vehicleService.deleteVehicle(id);
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

    @PostMapping("/repair-order/assign/update")
    public ResponseEntity<?> updateAssignment(@RequestParam Long assignmentId,
                                               @RequestParam AssignmentStatus status) {
        try {
           orderAssignmentService.updateAssignment(assignmentId, status);
            return ResponseEntity.ok(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new CommonErrorResponse(404, e.getMessage()));
        }
    }

    @DeleteMapping("/repair-order/assign")
    public ResponseEntity<?> deleteAssignment(@RequestParam Long assignmentId) {
        try {
            orderAssignmentService.deleteAssignment(assignmentId);
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

    private String generateRepairmanNumber() {
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder sb = new StringBuilder(16);
    Random random = new SecureRandom(); // 更安全的随机数生成器

    for (int i = 0; i < 16; i++) {
        sb.append(chars.charAt(random.nextInt(chars.length())));
    }

    return sb.toString();
}


}
