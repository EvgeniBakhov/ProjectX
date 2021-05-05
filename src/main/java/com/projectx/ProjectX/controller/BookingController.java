package com.projectx.ProjectX.controller;

import com.projectx.ProjectX.exceptions.InvalidBookingException;
import com.projectx.ProjectX.model.Booking;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.BookingRequest;
import com.projectx.ProjectX.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "booking")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('BOOK_ESTATE')")
    public ResponseEntity<Object> bookEstate(@AuthenticationPrincipal User user,
                                              @RequestBody BookingRequest bookingRequest) {
        Booking booking;
        try {
            booking = bookingService.bookEstate(user, bookingRequest);
        } catch (InvalidBookingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body(booking);
    }
}
