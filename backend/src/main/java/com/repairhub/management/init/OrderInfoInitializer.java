package com.repairhub.management.init;

import java.time.LocalDateTime;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.repairhub.management.repair.entity.RepairFeedback;
import com.repairhub.management.repair.entity.RepairRecord;
import com.repairhub.management.repair.enums.FeedbackType;
import com.repairhub.management.repair.repository.RepairFeedbackRepository;
import com.repairhub.management.repair.repository.RepairRecordRepository;

@Component
@Order(10)
public class OrderInfoInitializer implements ApplicationRunner{

    private final RepairFeedbackRepository feedbackRepository;
    private final RepairRecordRepository recordRepository;

    public OrderInfoInitializer(
        RepairFeedbackRepository feedbackRepository,
        RepairRecordRepository recordRepository
    ){
        this.feedbackRepository = feedbackRepository;
        this.recordRepository = recordRepository;
    }
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        RepairFeedback feedback = RepairFeedback.builder()
        .orderId(1L)
        .userId(1L)
        .rating(4)
        .feedbackType(FeedbackType.RATING)
        .description("到目前为止，服务还不错")
        .feedbackTime(LocalDateTime.now())
        .build();
        feedbackRepository.insert(feedback);
        RepairRecord record = RepairRecord.builder()
        .orderId(1L)
        .repairmanId(2L)
        .faultDescription("用太久了需要修复")
        .repairResult("初期修理一切顺利")
        .completionTime(LocalDateTime.now())
        .build();
        recordRepository.insert(record);
    }

}
