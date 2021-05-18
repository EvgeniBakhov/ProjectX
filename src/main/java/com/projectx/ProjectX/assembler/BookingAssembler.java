package com.projectx.ProjectX.assembler;

import com.projectx.ProjectX.enums.BookingStatus;
import com.projectx.ProjectX.model.Booking;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.BookingRequest;
import com.projectx.ProjectX.model.resource.UpdateBookingRequest;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class BookingAssembler {

    public Booking fromBookingRequest(BookingRequest request, User user) {
        Booking booking = new Booking();
        booking.setEstate(request.getEstate());
        booking.setFromDate(request.getFromDate());
        booking.setToDate(request.getToDate());
        booking.setUser(user);
        booking.setTotalPrice(calculateTotalPrice(booking));
        booking.setStatus(BookingStatus.CREATED);
        booking.setComment(request.getComment());
        return booking;
    }

    public Booking fromUpdateRequest(UpdateBookingRequest request, Booking booking) {
        booking.setFromDate(request.getFromDate());
        booking.setToDate(request.getToDate());
        booking.setTotalPrice(calculateTotalPrice(booking));
        booking.setStatus(BookingStatus.CREATED);
        booking.setComment(request.getComment());
        return booking;
    }

    private double calculateTotalPrice(Booking booking) {
        long diffInMillis = Math.abs(booking.getToDate().getTime() - booking.getFromDate().getTime());
        long dayDiff = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        return booking.getEstate().getRentPrice() * dayDiff;
    }
}
