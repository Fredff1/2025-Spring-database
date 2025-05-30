package com.repairhub.management.order.enums;

public enum AssignmentStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    CANCELED;

    public boolean isAccepted(){
        return this.equals(ACCEPTED);
    }
}
