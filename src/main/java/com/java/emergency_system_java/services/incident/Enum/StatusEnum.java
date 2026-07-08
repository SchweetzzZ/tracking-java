package com.java.emergency_system_java.services.incident.Enum;

public enum StatusEnum {
    PENDING(1),
    IN_PROGRESS(2),
    RESOLVED(3);

    private int code;

    private StatusEnum (int code) {
        this.code = code;
    }

    private int getCode(){
        return code;
    }

    public static StatusEnum valueOf(int code){
        for(StatusEnum value : StatusEnum.values()){
            if(value.getCode()==code){
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid statusEnum code");
    }
}
