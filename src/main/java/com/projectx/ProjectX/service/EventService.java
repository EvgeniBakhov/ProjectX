package com.projectx.ProjectX.service;

import com.projectx.ProjectX.assembler.EventAssembler;
import com.projectx.ProjectX.model.Event;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.EventCreateRequest;
import com.projectx.ProjectX.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    public boolean createEvent(EventCreateRequest request, User user) {
        EventAssembler eventAssembler = new EventAssembler();
        Event event = eventAssembler.fromCreateRequest(request, user);
        try {
            eventRepository.save(event);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
