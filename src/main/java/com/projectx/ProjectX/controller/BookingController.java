package com.projectx.ProjectX.controller;

import com.projectx.ProjectX.exceptions.EntityNotFoundException;
import com.projectx.ProjectX.exceptions.InvalidBookingException;
import com.projectx.ProjectX.model.Booking;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.BookingRequest;
import com.projectx.ProjectX.model.resource.BookingResponseResource;
import com.projectx.ProjectX.model.resource.UpdateBookingRequest;
import com.projectx.ProjectX.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/{bookingId}")
    public ResponseEntity updateBooking(@PathVariable("bookingId") Long bookingId,
                                              @AuthenticationPrincipal User user,
                                              @RequestBody UpdateBookingRequest request) {
        try {
            bookingService.updateBooking(bookingId, user, request);
        } catch (InvalidBookingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    //Maybe we can use the upper one
    @PutMapping()
    public ResponseEntity<Void> cancelBooking() {
        return ResponseEntity.ok().build();
    }


    public ResponseEntity<Void> approveBooking() {
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<List<BookingResponseResource>> findBookingsForEstate() {
        return ResponseEntity.ok().build();
    }

    public ResponseEntity findRelevantBookingsForEstate() {
        return ResponseEntity.ok().build();
    }

    public ResponseEntity findApprovedBookingsForEstate() {
        return ResponseEntity.ok().build();
    }

    public ResponseEntity findBookingsBetweenTwoDatesForEstate() {
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<BookingResponseResource> findBookingById() {
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> findBookingsForCurrentUser() {
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> findBookingsForUser() {
        return ResponseEntity.ok().build();
    }
}
