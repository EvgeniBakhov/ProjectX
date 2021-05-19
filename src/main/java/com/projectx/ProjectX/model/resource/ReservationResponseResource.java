package com.projectx.ProjectX.model.resource;

import com.projectx.ProjectX.enums.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationResponseResource {

    private Long id;
    private EventResponseResource event;
    private Double price;
    private UserResponseResource user;
    private ReservationStatus status;
}
