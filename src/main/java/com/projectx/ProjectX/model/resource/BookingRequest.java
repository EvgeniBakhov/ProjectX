package com.projectx.ProjectX.model.resource;

import com.projectx.ProjectX.model.Estate;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BookingRequest {
    private Estate estate;
    private Date fromDate;
    private Date toDate;
    private String comment;
}
