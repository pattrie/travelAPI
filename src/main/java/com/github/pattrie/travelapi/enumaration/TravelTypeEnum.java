package com.github.pattrie.travelapi.enumaration;

public enum TravelTypeEnum {
    RETURN("RETURN"),
    ONE_WAY("ONE-WAY"),
    MULTI_CITY("MULTI-CITY");

    private String value;

    TravelTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TravelTypeEnum getEnum(String value) {
        for (TravelTypeEnum t : values()) {
            if (value.equals(t.getValue())) {
                return t;
            }
        }
        throw new RuntimeException("Type not found.");
    }
}