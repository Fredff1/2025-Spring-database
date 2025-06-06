package com.repairhub.management.repairman.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.pulsar.PulsarProperties.Transaction;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalEventPublisher;

import com.repairhub.management.auth.entity.User;
import com.repairhub.management.auth.repository.UserRepository;
import com.repairhub.management.order.entity.OrderAssignment;
import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.order.enums.AssignmentStatus;
import com.repairhub.management.order.enums.OrderStatus;
import com.repairhub.management.order.event.AssignmentCopedEvent;
import com.repairhub.management.order.repository.OrderAssignmentRepository;
import com.repairhub.management.order.repository.RepairOrderRepository;
import com.repairhub.management.repair.dto.LaborFeeLogDTO;
import com.repairhub.management.repair.entity.LaborFeeLog;
import com.repairhub.management.repair.entity.RepairFeedback;
import com.repairhub.management.repair.entity.RepairRecord;
import com.repairhub.management.repair.enums.FaultType;
import com.repairhub.management.repair.repository.LaborFeeLogRepository;
import com.repairhub.management.repair.repository.RepairFeedbackRepository;
import com.repairhub.management.repair.repository.RepairRecordRepository;
import com.repairhub.management.repairman.dto.RepairmanBaseInfoDTO;
import com.repairhub.management.repairman.dto.RepairmanIncomeDTO;
import com.repairhub.management.repairman.dto.RepairmanOverviewDTO;
import com.repairhub.management.repairman.dto.RepairmanProfileDTO;
import com.repairhub.management.repairman.dto.RepairmanProfileUpdateDTO;
import com.repairhub.management.repairman.dto.RepairmanStatisticDTO;
import com.repairhub.management.repairman.entity.RepairmanProfile;
import com.repairhub.management.repairman.repository.RepairmanDashboardRepository;
import com.repairhub.management.repairman.repository.RepairmanIncomeRepository;
import com.repairhub.management.repairman.repository.RepairmanProfileRepository;
import com.repairhub.management.user.dto.UserProfileDTO;
import com.repairhub.management.utils.PageUtils;

@Service
public class RepairmanProfileService {

    private final UserRepository userRepository;
    private final RepairmanProfileRepository repairmanProfileRepository;
    private final OrderAssignmentRepository orderAssignmentRepository;
    private final RepairOrderRepository orderRepository;
    private final RepairRecordRepository recordRepository;
    private final LaborFeeLogRepository laborFeeLogRepository;
    private final RepairmanIncomeRepository repairmanIncomeRepository;
    private final RepairmanDashboardRepository repairmanDashboardRepository;
    private final ApplicationEventPublisher eventPublisher;

    public RepairmanProfileService(
            UserRepository userRepository,
            RepairmanProfileRepository repairmanProfileRepository,
            OrderAssignmentRepository orderAssignmentRepository,
            RepairOrderRepository orderRepository,
            RepairRecordRepository recordRepository,
            LaborFeeLogRepository laborFeeLogRepository,
            RepairmanIncomeRepository repairmanIncomeRepository,
            RepairmanDashboardRepository repairmanDashboardRepository,
            ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.repairmanProfileRepository = repairmanProfileRepository;
        this.orderAssignmentRepository = orderAssignmentRepository;
        this.orderRepository = orderRepository;
        this.recordRepository = recordRepository;
        this.laborFeeLogRepository = laborFeeLogRepository;
        this.repairmanIncomeRepository = repairmanIncomeRepository;
        this.repairmanDashboardRepository = repairmanDashboardRepository;
        this.eventPublisher = eventPublisher;
    }

    public List<RepairmanProfile> findAllWithFilter(FaultType specialty, List<Long> filterIds) {
        List<RepairmanProfile> profiles = null;
        if (specialty == null) {
            profiles = repairmanProfileRepository.findAll();
        } else {
            profiles = repairmanProfileRepository.findBySpecialty(specialty);
        }

        if (filterIds != null && !filterIds.isEmpty()) {
            profiles.removeIf(profile -> filterIds.contains(profile.getUserId()));
        }

        return profiles;
    }

    @Transactional
    public void copeWithOrderAssignment(OrderAssignment assignment, boolean accepted) {
        RepairmanProfile profile = repairmanProfileRepository.findByUserId(assignment.getRepairmanId())
                .orElseThrow(() -> new RuntimeException("维修工档案不存在"));
        if (accepted) {
            assignment.setStatus(AssignmentStatus.ACCEPTED);
        } else {
            assignment.setStatus(AssignmentStatus.REJECTED);
        }
        orderAssignmentRepository.update(assignment);
        completeProfile(profile);
        AssignmentCopedEvent event = new AssignmentCopedEvent(this, profile, assignment);
        eventPublisher.publishEvent(event);

    }

    public RepairmanProfile completeProfile(RepairmanProfile profile) {
        List<OrderAssignment> assignments = orderAssignmentRepository.findByRepairmanId(profile.getUserId());
        if (assignments != null && !assignments.isEmpty()) {
            profile.setOrderAssignments(assignments);
        } else {
            profile.setOrderAssignments(List.of());
        }
        return profile;
    }

    public List<RepairOrder> findRepairmanOrders(User repairman) {
        Long repairmanId = repairman.getUserId();
        List<OrderAssignment> assignments = orderAssignmentRepository.findByRepairmanId(repairmanId);
        List<OrderAssignment> filteredAssignments = assignments.stream()
                .filter(assignment -> assignment.getStatus().isAccepted())
                .collect(Collectors.toList());
        List<RepairOrder> orders = filteredAssignments.stream()
                .map(assignment -> orderRepository.findById(assignment.getOrderId()).get())
                .collect(Collectors.toList());
        return orders;
    }

    public RepairmanProfileDTO getProfileDTO(User repairman) {
        Long id = repairman.getUserId();
        RepairmanProfile profile = repairmanProfileRepository.findByUserId(id).get();
        RepairmanProfileDTO dto = RepairmanProfileDTO.builder()
                .id(id)
                .username(repairman.getUsername())
                .phone(repairman.getPhone())
                .email(repairman.getEmail())
                .createTime(repairman.getCreatedAt())
                .updateTime(repairman.getUpdatedAt())
                .userStatus(repairman.getStatus())
                .specialty(profile.getSpecialty())
                .hourlyMoneyRate(profile.getHourlyMoneyRate())
                .repairmanNumber(profile.getRepairmanNumber())
                .build();
        return dto;
    }

    public RepairmanBaseInfoDTO getBaseInfoDTO(Long repairmanId){
        RepairmanProfile profile = repairmanProfileRepository.findByUserId(repairmanId).get();
        User repairman = userRepository.findById(repairmanId).get();
        return RepairmanBaseInfoDTO.from(profile, repairman);
    }

    public RepairmanStatisticDTO getStatistics(User repairman, LocalDateTime startDateTime) {
        if (startDateTime == null) {
            startDateTime = LocalDateTime.of(LocalDate.of(1980, 1, 1), LocalTime.of(1, 1, 1));
        }
        final LocalDateTime startTime = startDateTime;

        Integer completedOrdersCount = repairmanDashboardRepository.countCompletedOrdersAfter(repairman.getUserId(), startTime);
        
        BigDecimal totalWorkHour = repairmanDashboardRepository.sumWorkHoursAfter(repairman.getUserId(), startTime);
       
        BigDecimal totalIncome = repairmanDashboardRepository.sumIncomeAfter(repairman.getUserId(), startTime);
        
        double averageRating = repairmanDashboardRepository.averageRatingAfter(repairman.getUserId(), startTime);

        RepairmanStatisticDTO dto = RepairmanStatisticDTO.builder()
                .completedOrders(completedOrdersCount)
                .repairHours(totalWorkHour)
                .totalIncome(totalIncome)
                .averageRating(averageRating)
                .build();
        return dto;
    }
    public RepairmanOverviewDTO getOverview(User repairman) {
        Long repairmanId = repairman.getUserId();
        LocalDateTime startTime = LocalDateTime.of(LocalDate.of(1980, 1, 1), LocalTime.of(1, 1, 1));
        LocalDateTime now = LocalDateTime.now();
        int totalAssignments = repairmanDashboardRepository.countTotalAssignments(repairmanId, startTime, now);
        int processingOrders = repairmanDashboardRepository.countProcessingOrders(repairmanId);
        BigDecimal monthlyIncome = repairmanDashboardRepository.sumMonthlyIncome(repairmanId, now.minusMonths(1L), now);
        double rating = repairmanDashboardRepository.averageRatingAllTime(repairmanId);
        RepairmanOverviewDTO dto = RepairmanOverviewDTO.builder()
        .totalAssignments(totalAssignments)
        .processingOrders(processingOrders)
        .monthlyIncome(monthlyIncome)
        .rating(rating)
        .build();
        return dto;
    }

    public RepairmanIncomeDTO getIncomeStatistics(
        User repairman,
        int page,
        int limit,
        LocalDate startTime,
        LocalDate endTime
    ){  
        Long repairmanId = repairman.getUserId();
        RepairmanProfile profile = repairmanProfileRepository.findByUserId(repairmanId)
            .orElseThrow(() -> new RuntimeException("维修工档案不存在"));
        List<LaborFeeLog> logs = repairmanIncomeRepository.findFeeLogs(repairmanId, startTime, endTime);
        List<LaborFeeLog> pagedLogs = PageUtils.paginate(logs, page, limit);
        BigDecimal totalIncome = repairmanIncomeRepository.sumTotalIncome(repairmanId, startTime, endTime);
        BigDecimal totalWorkHour = repairmanIncomeRepository.sumTotalHours(repairmanId, startTime, endTime);
        BigDecimal averageHourlyRate = BigDecimal.ZERO;
        if (totalWorkHour.compareTo(BigDecimal.ZERO) > 0) {
            averageHourlyRate = totalIncome.divide(totalWorkHour,RoundingMode.HALF_UP);
        } else {
            averageHourlyRate = profile.getHourlyMoneyRate();
        }

        List<LaborFeeLogDTO> logDTOs = pagedLogs.stream()
            .map(log -> LaborFeeLogDTO.from(log, profile,repairman))
            .collect(Collectors.toList());
        RepairmanIncomeDTO dto =RepairmanIncomeDTO.builder()
            .total(logs.size())
            .list(logDTOs)
            .totalIncome(totalIncome)
            .totalHours(totalWorkHour)
            .averageHourlyRate(averageHourlyRate)
            .build();
        return dto;
    }

    public UserProfileDTO updateProfile(Long userId, RepairmanProfileUpdateDTO dto){
        userRepository.updateBasicInfo(userId, dto.getUsername(), dto.getPhone(), dto.getEmail());
        repairmanProfileRepository.update(userId, dto);
        User updatedUser = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserProfileDTO().builder()
            .id(updatedUser.getUserId())
            .username(updatedUser.getUsername())
            .phone(updatedUser.getPhone())
            .email(updatedUser.getEmail())
            .createTime(updatedUser.getCreatedAt())
            .updateTime(updatedUser.getUpdatedAt())
            .userStatus(updatedUser.getStatus())
            .build();
    }

   

}
