package com.repairhub.management.order.event;

import org.springframework.context.ApplicationEvent;

import com.repairhub.management.order.entity.RepairOrder;
import com.repairhub.management.repair.dto.CreateLaborFeeLogDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDealtEvent extends ApplicationEvent{
    private RepairOrder repairOrder;
    private CreateLaborFeeLogDTO createLaborFeeLogDTO;

    public OrderDealtEvent(
        Object source, 
        RepairOrder repairOrder,
        CreateLaborFeeLogDTO createLaborFeeLogDTO) {
        super(source);
        this.repairOrder = repairOrder;
        this.createLaborFeeLogDTO = createLaborFeeLogDTO;
    }
}
