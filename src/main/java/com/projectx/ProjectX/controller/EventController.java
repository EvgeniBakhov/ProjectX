package com.projectx.ProjectX.controller;

import com.projectx.ProjectX.exceptions.EntityNotFoundException;
import com.projectx.ProjectX.exceptions.UpdateNotAllowedException;
import com.projectx.ProjectX.model.Event;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.EventCreateRequest;
import com.projectx.ProjectX.model.resource.EventUpdateRequest;
import com.projectx.ProjectX.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "event")
public class EventController {

    @Autowired
    EventService eventService;

    @PreAuthorize("hasAuthority('PUBLISH_EVENT')")
    @PostMapping()
    public ResponseEntity<Event> createEvent(@RequestBody EventCreateRequest request, @AuthenticationPrincipal User user) {
        eventService.createEvent(request, user);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{eventId}")
    public ResponseEntity updateEvent(@PathVariable Long eventId,
                                             @RequestBody EventUpdateRequest request,
                                             @AuthenticationPrincipal User user) {
        try {
            eventService.updateEvent(eventId, request, user);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UpdateNotAllowedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{eventId}")
    public ResponseEntity deleteEvent(@PathVariable Long eventId, @AuthenticationPrincipal User user) {
        try {
            eventService.deleteEvent(eventId, user);
        } catch (UpdateNotAllowedException e) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (EntityNotFoundException e) {
            ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/{eventId}")
    public ResponseEntity<Void> addPhotos() {
        return ResponseEntity.ok().build();
    }
}
