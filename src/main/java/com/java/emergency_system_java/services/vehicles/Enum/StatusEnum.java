package com.java.emergency_system_java.services.vehicles.Enum;

public enum StatusEnum {
    AVALIABLE(1),
    DISPACHED(2),
    BUSY(3),
    OFFLINE(4),
    EN_ROUTE(5),
    AT_INCIDENT(6);

    private int code;

    private StatusEnum(int code) {
        this.code = code;
    }

    private int getCode() {
        return code;
    }

    public static StatusEnum valueOf(int code) {
        for (StatusEnum value : StatusEnum.values()) {
            if(value.getCode() == code){
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid statusEnum code");
    }
}

