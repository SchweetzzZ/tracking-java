package com.java.emergency_system_java.services.incident.Enum;

public enum IncidentStatus {
    PENDING(1),
    IN_PROGRESS(2),
    RESOLVED(3);

    private final int code;

    private IncidentStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static IncidentStatus valueOf(int code) {
        for (IncidentStatus value : IncidentStatus.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid status code");
    }
}
