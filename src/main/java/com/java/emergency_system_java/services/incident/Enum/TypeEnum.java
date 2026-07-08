package com.java.emergency_system_java.services.incident.Enum;

public enum TypeEnum {
    ACCIDENT(1),
    FIRE(2),
    MEDICAL(3);

    private int code;

    private TypeEnum(int code){
        this.code = code;
    }

    private int getCode(){
        return code;
    }

    public static TypeEnum valueOf(int code){
        for(TypeEnum value : TypeEnum.values()){
            if(value.getCode() == code){
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid statusEnum code");
    }
}
