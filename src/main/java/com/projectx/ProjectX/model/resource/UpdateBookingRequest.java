package com.projectx.ProjectX.model.resource;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UpdateBookingRequest {

    private Long id;
    private Date fromDate;
    private Date toDate;
    private String comment;
}
