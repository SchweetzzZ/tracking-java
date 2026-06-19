package com.java.emergency_system_java.services.Enum;

public enum TypeEnum {
    CART(1),
    AMBULANCE(2),
    FIRE_TRUCK(3),
    POLICE_CAR(4),
    TOW_TRUCK(5);

    private int code;

    private TypeEnum(int code){this.code = code;}

    public int getCode() {return code;}

    public static TypeEnum valueOf(int code) {
        for (TypeEnum value : TypeEnum.values()) {
            if(value.getCode() == code){
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid typeEnum code");
    }
}
