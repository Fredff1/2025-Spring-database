package com.repairhub.management.order.event;

import org.springframework.context.ApplicationEvent;

import com.repairhub.management.order.entity.RepairOrder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreatedEvent extends ApplicationEvent{
    
    private RepairOrder repairOrder;

    public OrderCreatedEvent(Object source,RepairOrder repairOrder) {
        super(source);
        this.repairOrder = repairOrder;
    }


}
