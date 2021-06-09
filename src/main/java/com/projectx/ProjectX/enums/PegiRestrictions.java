package com.projectx.ProjectX.enums;

public enum PegiRestrictions {

    EVERYONE(3), EVERYONE_SEVEN_PLUS(7), TEEN(12), MATURE(16),
    ADULT(18);

    private final int minimalAge;

    PegiRestrictions(int minimalAge){
        this.minimalAge = minimalAge;
    }

    public int getValue() {
        return minimalAge;
    }
}
