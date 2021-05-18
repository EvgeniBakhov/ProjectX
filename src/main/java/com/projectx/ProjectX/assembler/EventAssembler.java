package com.projectx.ProjectX.assembler;

import com.projectx.ProjectX.enums.EventStatus;
import com.projectx.ProjectX.model.Event;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.EventCreateRequest;
import com.projectx.ProjectX.model.resource.EventUpdateRequest;

public class EventAssembler {

    public Event fromCreateRequest(EventCreateRequest request, User user) {
        Event event = new Event();
        event.setName(request.getName());
        event.setDescription(request.getDescription());
        event.setStartDate(request.getStartDate());
        event.setEndDate(request.getEndDate());
        event.setAddress(request.getAddress());
        event.setCapacity(request.getCapacity());
        event.setAvailableSeats(request.getCapacity());
        event.setAgeRestrictions(request.getAgeRestrictions());
        event.setType(request.getType());
        event.setPlaceType(request.getPlaceType());
        event.setOrganizer(user);
        event.setStatus(EventStatus.PLANNED);
        event.setCreatedBy(user.getId().toString());
        event.setModifiedBy(user.getId().toString());
        return event;
    }

    public Event fromUpdateRequest(EventUpdateRequest request, Event existingEvent) {
        existingEvent.setName(request.getName());
        existingEvent.setDescription(request.getDescription());
        existingEvent.setStartDate(request.getStartDate());
        existingEvent.setEndDate(request.getEndDate());
        existingEvent.setCapacity(request.getCapacity());
        existingEvent.setAvailableSeats(request.getAvailableSeats());
        existingEvent.setAgeRestrictions(request.getAgeRestrictions());
        existingEvent.setAddress(request.getAddress());
        existingEvent.setType(request.getType());
        existingEvent.setPlaceType(request.getPlaceType());
        existingEvent.setPlaceType(request.getPlaceType());
        return existingEvent;
    }
}
