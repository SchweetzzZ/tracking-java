package com.java.emergency_system_java.services.vehicles.Enum;

public enum VehicleStatus {
    AVAILABLE(1),
    DISPACHED(2),
    BUSY(3),
    OFFLINE(4),
    EN_ROUTE(5),
    AT_INCIDENT(6);

    private final int code;

    private VehicleStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static VehicleStatus valueOf(int code) {
        for (VehicleStatus value : VehicleStatus.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid status code");
    }
}
