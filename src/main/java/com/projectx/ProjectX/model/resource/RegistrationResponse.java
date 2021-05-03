package com.projectx.ProjectX.model.resource;

import com.projectx.ProjectX.enums.resource.RegistrationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationResponse {

    private RegistrationStatus status;
    private String message;
}
