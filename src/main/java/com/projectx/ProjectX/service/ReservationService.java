package com.projectx.ProjectX.service;

import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.ReservationResponseResource;
import com.projectx.ProjectX.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    @Autowired
    ReservationRepository reservationRepository;


    public ReservationResponseResource createReservation(Long eventId, User user) {
        return null;
    }

    private boolean checkPlaces() {
        return false;
    }

    private boolean checkEsrbRate() {
        return false;
    }

    private boolean checkEventStatus() {
        return false;
    }

    private boolean checkExistingReservation() {
        return false;
    }
}
