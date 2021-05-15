package com.projectx.ProjectX.controller;

import com.projectx.ProjectX.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {

    @Autowired
    ReservationService reservationService;
}
