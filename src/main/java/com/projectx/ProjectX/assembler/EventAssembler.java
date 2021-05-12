package com.projectx.ProjectX.assembler;

import com.projectx.ProjectX.enums.EventStatus;
import com.projectx.ProjectX.model.Event;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.EventCreateRequest;

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
}
