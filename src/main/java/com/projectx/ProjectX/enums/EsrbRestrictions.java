package com.projectx.ProjectX.enums;

import lombok.Getter;

public enum EsrbRestrictions {

    EVERYONE(0), EVERYONE_TEN_PLUS(10), TEEN(14), MATURE(16),
    ADULT(18);

    private final int minimalAge;

    EsrbRestrictions(int minimalAge){
        this.minimalAge = minimalAge;
    }

    public int getValue() {
        return minimalAge;
    }
}
