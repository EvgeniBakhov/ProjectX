package com.projectx.ProjectX.controller;

import com.projectx.ProjectX.model.Estate;
import com.projectx.ProjectX.service.EstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping(value = "/estate")
public class EstateController {

    @Autowired
    EstateService estateService;

    @PutMapping()
    public ResponseEntity<Void> publishEstate(@RequestBody Estate estate) {
        if(estateService.publishEstate(estate)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{estateId}")
    public ResponseEntity<Estate> getEstateById(@PathVariable Long estateId) {
        return ResponseEntity.of(estateService.findEstateById(estateId));
    }

    @GetMapping()
    public ResponseEntity<List<Estate>> getEstatesByUserId(@PathParam("owner") Long ownerId) {
        return ResponseEntity.of(estateService.findEstatesByOwnerId(ownerId));
    }
}
