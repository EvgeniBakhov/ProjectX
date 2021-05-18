package com.projectx.ProjectX.model.resource;

import com.projectx.ProjectX.enums.EsrbRestrictions;
import com.projectx.ProjectX.enums.EventPlaceType;
import com.projectx.ProjectX.enums.EventStatus;
import com.projectx.ProjectX.enums.EventType;
import com.projectx.ProjectX.model.Address;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EventResponseResource {

    private Long id;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private int capacity;
    private int availableSeats;
    private EsrbRestrictions ageRestrictions;
    private Address address;
    private UserResponseResource organizer;
    private EventType type;
    private EventPlaceType placeType;
    private EventStatus status;

}
