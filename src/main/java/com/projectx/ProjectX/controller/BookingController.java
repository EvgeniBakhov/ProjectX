package com.projectx.ProjectX.controller;

import com.projectx.ProjectX.model.Booking;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "booking")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @PostMapping()
    public ResponseEntity<Booking> bookEstate(@AuthenticationPrincipal User user, ) {
        return bookingService.bookEstate();
    }
}
