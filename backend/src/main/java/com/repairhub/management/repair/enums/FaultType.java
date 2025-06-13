package com.repairhub.management.repair.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum FaultType {
    MAINTENANCE,       // 发动机
    REPAIR,   // 电气
    PAINT,        // 刹车
    TIRE, // 传动
    OTHER,
    ELECTRICAL, 
    BODYWORK,
    ENGINE;


    @JsonCreator
    public static FaultType from(String value) {
        return FaultType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
