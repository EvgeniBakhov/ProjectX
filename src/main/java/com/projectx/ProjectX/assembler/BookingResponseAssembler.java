package com.projectx.ProjectX.assembler;

import com.projectx.ProjectX.model.Booking;
import com.projectx.ProjectX.model.resource.BookingResponseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        resource.setTotalPrice(booking.getTotalPrice());
        return resource;
    }

    public List<BookingResponseResource> fromBookingsList(List<Booking> bookings) {
        if(bookings == null) {
            return null;
        }
        List<BookingResponseResource> resourceList = new ArrayList<>();
        bookings.stream().forEach(booking -> resourceList.add(fromBooking(booking)));
        return resourceList;
    }
}
