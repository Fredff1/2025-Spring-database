package com.repairhub.management.init;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.repairhub.management.auth.domain.enums.UserRole;
import com.repairhub.management.auth.domain.enums.UserStatus;
import com.repairhub.management.auth.entity.User;
import com.repairhub.management.auth.repository.UserRepository;
import com.repairhub.management.order.entity.OrderAssignment;
import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.order.enums.AssignmentStatus;
import com.repairhub.management.order.enums.OrderStatus;
import com.repairhub.management.order.repository.OrderAssignmentRepository;
import com.repairhub.management.order.repository.RepairOrderRepository;
import com.repairhub.management.repair.enums.FaultType;
import com.repairhub.management.repair.enums.RepairType;
import com.repairhub.management.repairman.entity.RepairmanProfile;
import com.repairhub.management.repairman.repository.RepairmanProfileRepository;
import com.repairhub.management.vehicle.entity.Vehicle;
import com.repairhub.management.vehicle.repository.VehicleRepository;

@Component
@Order(5)
public class DataInitializer implements ApplicationRunner{
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final RepairmanProfileRepository repairmanProfileRepository;
    private final RepairOrderRepository repairOrderRepository;
    private final OrderAssignmentRepository orderAssignmentRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
        UserRepository userRepository, 
        RepairmanProfileRepository repairmanProfileRepository,
        VehicleRepository vehicleRepository,
        RepairOrderRepository repairOrderRepository,
        OrderAssignmentRepository orderAssignmentRepository,
        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.repairmanProfileRepository = repairmanProfileRepository;
        this.vehicleRepository = vehicleRepository;
        this.repairOrderRepository = repairOrderRepository;
        this.orderAssignmentRepository = orderAssignmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initUser();
        initVehicles();
        initOrders();
    }

    private void initUser(){
        User customer = User.builder()
            .username("alice")
            .password(passwordEncoder.encode("alice123"))
            .email("alice@example.com")
            .phone("123-456-7890")
            .role(UserRole.CUSTOMER)
            .status(UserStatus.ACTIVE)
            .build();
        userRepository.insert(customer);

        User repairman = User.builder()
            .username("bob")
            .password(passwordEncoder.encode("bob123"))
            .email("bob@repairhub.com")
            .phone("234-567-8901")
            .role(UserRole.REPAIRMAN)
            .status(UserStatus.ACTIVE)
            .build();
        userRepository.insert(repairman);
        RepairmanProfile repairmanProfile = RepairmanProfile.builder()
            .userId(repairman.getUserId())
            .specialty(FaultType.MAINTENANCE)
            .hourlyMoneyRate(new BigDecimal("100.00"))
            .build();
        repairmanProfileRepository.insert(repairmanProfile);
        
        User admin = User.builder()
            .username("admin")
            .password(passwordEncoder.encode("adminl123"))
            .email("admin@repairhub.com")
            .phone("345-678-9012")
            .role(UserRole.ADMIN)
            .status(UserStatus.ACTIVE)
            .build();
        
        userRepository.insert(admin);
    }

    private void initVehicles(){
        User alice = userRepository.findByUsername("alice").get();
        Vehicle vehicle = Vehicle.builder()
        .ownerId(alice.getUserId())
        .brand("Toyota")
        .model("v100")
        .licensePlate("沪A123ZX")
        .registerDate(LocalDate.now())
        .build();
        vehicleRepository.insert(vehicle);
    }

    private void initOrders(){
        User alice = userRepository.findByUsername("alice").get();
        User repairman = userRepository.findByUsername("bob").get();
        RepairOrder order = RepairOrder.builder()
        .userId(alice.getUserId())
        .vehicleId(1L)
        .submitTime(LocalDateTime.now())
        .updateTime(LocalDateTime.now())
        .status(OrderStatus.PENDING)
        .description("默认维修任务")
        .faultType(FaultType.MAINTENANCE)
        .totalFee(BigDecimal.valueOf(0L))
        .isPaid(false)
        .build();
        repairOrderRepository.insert(order);

        OrderAssignment assignment = OrderAssignment.builder()
        .orderId(order.getOrderId())
        .repairmanId(repairman.getUserId())
        .assignmentTime(LocalDateTime.now())
        .status(AssignmentStatus.PENDING)
        .build();
        orderAssignmentRepository.insert(assignment);

    }
    
}
