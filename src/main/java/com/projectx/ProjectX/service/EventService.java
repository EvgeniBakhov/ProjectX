package com.projectx.ProjectX.service;

import com.projectx.ProjectX.assembler.EventAssembler;
import com.projectx.ProjectX.exceptions.EntityNotFoundException;
import com.projectx.ProjectX.exceptions.UpdateNotAllowedException;
import com.projectx.ProjectX.model.Event;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.EventCreateRequest;
import com.projectx.ProjectX.assembler.EventResponseAssembler;
import com.projectx.ProjectX.model.resource.EventResponseResource;
import com.projectx.ProjectX.model.resource.EventUpdateRequest;
import com.projectx.ProjectX.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventResponseAssembler eventResponseAssembler;

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

    public void updateEvent(Long eventId, EventUpdateRequest request, User user) throws EntityNotFoundException, UpdateNotAllowedException {
        Optional<Event> existingEvent = eventRepository.findById(eventId);
        EventAssembler eventAssembler = new EventAssembler();
        if (existingEvent.isPresent()) {
            if (existingEvent.get().getOrganizer().getId().equals(user.getId())) {
                Event updatedEvent = eventAssembler.fromUpdateRequest(request, existingEvent.get());
                eventRepository.save(updatedEvent);
            } else {
                throw new UpdateNotAllowedException("You are not the organizer of this event.");
            }
        } else {
            throw new EntityNotFoundException("Event with this id does not exist.");
        }
    }

    public void deleteEvent(Long eventId, User user) throws UpdateNotAllowedException, EntityNotFoundException {
        Optional<Event> existingEvent = eventRepository.findById(eventId);
        if (existingEvent.isPresent()) {
            if (existingEvent.get().getOrganizer().getId().equals(user.getId())) {
                existingEvent.get().setDeleted(true);
                eventRepository.save(existingEvent.get());
            } else {
                throw new UpdateNotAllowedException("You are not the organizer of this event.");
            }
        } else {
            throw new EntityNotFoundException("Event with this id does not exist.");
        }
    }


    public EventResponseResource findEventById(Long eventId) throws EntityNotFoundException {
        Optional<Event> existingEvent = eventRepository.findById(eventId);
        if (existingEvent.isPresent()) {
            return eventResponseAssembler.fromEvent(existingEvent.get());
        } else {
            throw new EntityNotFoundException("Event with this id does not exist.");
        }
    }
}
