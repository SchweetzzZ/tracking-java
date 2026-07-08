package com.java.emergency_system_java.services.incident.Enum;

public enum AssignmentStatus {
    ASSIGNED(1),
    ACCEPTED(2),
    EN_ROUTE(3),
    ARRIVED(4),
    COMPLETED(5),
    CANCELLED(6);

    private final int code;

    private AssignmentStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static AssignmentStatus valueOf(int code) {
        for (AssignmentStatus value : AssignmentStatus.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid status code");
    }
}
