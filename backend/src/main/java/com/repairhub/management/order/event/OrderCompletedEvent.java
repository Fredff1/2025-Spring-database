package com.repairhub.management.order.event;

import org.springframework.context.ApplicationEvent;

import com.repairhub.management.order.entity.RepairOrder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCompletedEvent extends ApplicationEvent{
    private RepairOrder repairOrder;

    public OrderCompletedEvent(Object source, RepairOrder repairOrder) {
        super(source);
        this.repairOrder = repairOrder;
    }
}
