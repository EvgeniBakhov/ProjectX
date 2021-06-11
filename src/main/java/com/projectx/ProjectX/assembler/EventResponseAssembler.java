package com.projectx.ProjectX.assembler;

import com.projectx.ProjectX.assembler.UserResponseAssembler;
import com.projectx.ProjectX.model.Event;
import com.projectx.ProjectX.model.resource.EventResponseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventResponseAssembler {

    @Autowired
    UserResponseAssembler userResponseAssembler;

    public EventResponseResource fromEvent(Event event) {
        EventResponseResource eventResponseResource = new EventResponseResource();
        eventResponseResource.setId(event.getId());
        eventResponseResource.setName(event.getName());
        eventResponseResource.setDescription(event.getDescription());
        eventResponseResource.setStartDate(event.getStartDate());
        eventResponseResource.setEndDate(event.getEndDate());
        eventResponseResource.setCapacity(event.getCapacity());
        eventResponseResource.setAvailableSeats(event.getAvailableSeats());
        eventResponseResource.setAgeRestrictions(event.getAgeRestrictions());
        eventResponseResource.setAddress(event.getAddress());
        eventResponseResource.setOrganizer(userResponseAssembler.fromUser(event.getOrganizer()));
        eventResponseResource.setType(event.getType());
        eventResponseResource.setPlaceType(event.getPlaceType());
        eventResponseResource.setStatus(event.getStatus());
        eventResponseResource.setPictures(event.getPictures());
        eventResponseResource.setThumbnail(event.getThumbnail());
        return eventResponseResource;
    }
}
