package com.projectx.ProjectX.controller;

import com.projectx.ProjectX.model.Event;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.EventUpdateResource;
import com.projectx.ProjectX.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId,
                                             @RequestBody EventUpdateResource resource,
                                             @AuthenticationPrincipal User principal) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok().build();
    }
}
