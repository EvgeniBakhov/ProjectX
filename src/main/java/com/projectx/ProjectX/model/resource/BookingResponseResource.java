package com.projectx.ProjectX.model.resource;

import com.projectx.ProjectX.enums.BookingStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BookingResponseResource {

    private Long id;
    private EstateResponseResource estate;
    private Date fromDate;
    private Date toDate;
    private UserResponseResource user;
    private BookingStatus status;
    private String comment;
    private Double totalPrice;

}
