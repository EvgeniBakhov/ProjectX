package com.projectx.ProjectX.controller;

import com.projectx.ProjectX.assembler.BookingResponseAssembler;
import com.projectx.ProjectX.enums.BookingStatus;
import com.projectx.ProjectX.exceptions.EntityNotFoundException;
import com.projectx.ProjectX.exceptions.InvalidBookingException;
import com.projectx.ProjectX.exceptions.NotAllowedException;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "booking")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @Autowired
    BookingResponseAssembler assembler;

    @PostMapping(value = "/{estateId}")
    @PreAuthorize("hasAnyAuthority('BOOK_ESTATE')")
    public ResponseEntity bookEstate(@PathVariable Long estateId,
                                     @AuthenticationPrincipal User user,
                                     @RequestBody BookingRequest bookingRequest) {

        try {
            BookingResponseResource booking = bookingService.bookEstate(estateId, user, bookingRequest);
            return ResponseEntity.ok().body(booking);
        } catch (InvalidBookingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
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
    @GetMapping("/{bookingId}/cancel")
    public ResponseEntity cancelBooking(@PathVariable Long bookingId, @AuthenticationPrincipal User user) {
        try {
            bookingService.cancelBooking(bookingId, user);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (NotAllowedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{bookingId}/approve")
    public ResponseEntity approveBooking(@PathVariable Long bookingId,
                                               @AuthenticationPrincipal User user) {
        try {
            bookingService.approveBooking(bookingId, user);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (NotAllowedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/estate/{estateId}")
    public ResponseEntity findBookingsForEstate(@PathVariable Long estateId,
                                                @AuthenticationPrincipal User user) {
        try {
            List<BookingResponseResource> bookingsForEstate = bookingService.findBookingsForEstate(estateId, user);
            return ResponseEntity.ok().body(bookingsForEstate);
        } catch (NotAllowedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/estate/{estateId}/relevant")
    public ResponseEntity findRelevantBookingsForEstate(@PathVariable Long estateId,
                                                        @AuthenticationPrincipal User user) {
        try {
            List<BookingResponseResource> bookings = bookingService.findRelevantBookingsForEstate(estateId, user);
            return ResponseEntity.ok().body(bookings);
        } catch (NotAllowedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/estate/{estateId}/approved")
    public ResponseEntity findApprovedBookingsForEstate(@PathVariable Long estateId,
                                                        @AuthenticationPrincipal User user) {
        try {
            List<BookingResponseResource> bookings =
                    bookingService.findBookingsForEstateWithStatus(estateId, user, BookingStatus.APPROVED);
            return ResponseEntity.ok().body(bookings);

        } catch (NotAllowedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/estate/{estateId}/dates")
    public ResponseEntity findBookingsBetweenTwoDatesForEstate(@RequestParam(value = "fromDate") String from,
                                                               @RequestParam(value = "toDate") String to,
                                                               @AuthenticationPrincipal User user,
                                                               @PathVariable Long estateId) {
        try {
            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = sm.parse(from);
            Date toDate = sm.parse(to);
            List<BookingResponseResource> bookingsBetweenDates =
                    bookingService.findBookingsBetweenDates(estateId, fromDate, toDate, user);
            return ResponseEntity.ok().body(bookingsBetweenDates);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidBookingException | NotAllowedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body("Wrong format.");
        }
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity findBookingById(@PathVariable Long bookingId,
                                          @AuthenticationPrincipal User user) {
        try {
            BookingResponseResource response = bookingService.findById(bookingId, user);
            return ResponseEntity.ok().body(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (NotAllowedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseResource>> findBookingsForCurrentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.of(bookingService.findForCurrentUser(user));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity findBookingsForUser(@PathVariable Long userId,
                                              @AuthenticationPrincipal User user) {
        try {
            List<BookingResponseResource> userBookings = bookingService.findForUser(userId, user);
            return ResponseEntity.ok().body(userBookings);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
