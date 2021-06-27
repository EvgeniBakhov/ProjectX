package com.projectx.ProjectX.assembler;

import com.projectx.ProjectX.model.Reservation;
import com.projectx.ProjectX.model.resource.ReservationResponseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReservationResponseAssembler {

    @Autowired
    private UserResponseAssembler userResponseAssembler;

    @Autowired EventResponseAssembler eventResponseAssembler;

    public ReservationResponseResource fromReservation(Reservation reservation) {
        ReservationResponseResource resource = new ReservationResponseResource();
        resource.setId(reservation.getId());
        resource.setUser(userResponseAssembler.fromUser(reservation.getUser()));
        resource.setEvent(eventResponseAssembler.fromEvent(reservation.getEvent()));
        resource.setStatus(reservation.getStatus());
        return resource;
    }

    public List<ReservationResponseResource> fromReservationList(List<Reservation> reservations) {
        List<ReservationResponseResource> resources = new ArrayList<>();
        reservations.stream().forEach(reservation -> resources.add(fromReservation(reservation)));
        return resources;
    }
}
