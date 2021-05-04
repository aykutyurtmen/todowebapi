package com.daimontech.dsapi.model.enums;

public enum RecommendedPackageRateEnums {
    ZERO(0), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);

    private final int value;

    RecommendedPackageRateEnums(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
