package com.projectx.ProjectX.assembler;

import com.projectx.ProjectX.model.Booking;
import com.projectx.ProjectX.model.resource.BookingResponseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingResponseAssembler {

    @Autowired
    UserResponseAssembler userResponseAssembler;

    @Autowired
    EstateResponseAssembler estateResponseAssembler;

    public BookingResponseResource fromBooking(Booking booking) {
        BookingResponseResource resource = new BookingResponseResource();
        resource.setId(booking.getId());
        resource.setFromDate(booking.getFromDate());
        resource.setToDate(booking.getToDate());
        resource.setStatus(booking.getStatus());
        resource.setComment(booking.getComment());
        resource.setEstate(estateResponseAssembler.fromEstate(booking.getEstate()));
        resource.setUser(userResponseAssembler.fromUser(booking.getUser()));
        return resource;
    }
}
