package com.repairhub.management.order.event;

import org.springframework.context.ApplicationEvent;

import com.repairhub.management.auth.entity.RepairmanProfile;
import com.repairhub.management.order.entity.OrderAssignment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignmentCopedEvent extends ApplicationEvent{
    
    private RepairmanProfile repairmanProfile;
    private OrderAssignment orderAssignment;

    public AssignmentCopedEvent(Object source,RepairmanProfile repairmanProfile,OrderAssignment orderAssignment) {
        super(source);
        this.repairmanProfile = repairmanProfile;
        this.orderAssignment = orderAssignment;
    }
}
