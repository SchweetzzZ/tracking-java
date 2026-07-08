package com.java.emergency_system_java.services.incident.Enum;

import com.java.emergency_system_java.entity.Assigment;

public enum AssigmentStatusEnum {

    ASSIGNED(1),
    ACCEPTED(2),
    EN_ROUTE(3),
    ARRIVED(4),
    COMPLETED(5),
    CANCELLED(6);

    private int code;

    private AssigmentStatusEnum (int code){
        this.code = code;
    }

    private int getCode(){
        return code;
    }

    public AssigmentStatusEnum valueOf(int code) {
        for (AssigmentStatusEnum value : AssigmentStatusEnum.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid statusEnum code");
    }
}
