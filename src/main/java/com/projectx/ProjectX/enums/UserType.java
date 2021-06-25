package com.projectx.ProjectX.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserType {
    @JsonProperty("normal")
    NORMAL,
    @JsonProperty("owner")
    OWNER,
    @JsonProperty("organizer")
    ORGANIZER
}
