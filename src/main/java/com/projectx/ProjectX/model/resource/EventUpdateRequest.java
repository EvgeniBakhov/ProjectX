package com.projectx.ProjectX.model.resource;

import com.projectx.ProjectX.enums.PegiRestrictions;
import com.projectx.ProjectX.enums.EventPlaceType;
import com.projectx.ProjectX.enums.EventStatus;
import com.projectx.ProjectX.enums.EventType;
import com.projectx.ProjectX.model.Address;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EventUpdateRequest {

    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private int capacity;
    private int availableSeats;
    private PegiRestrictions ageRestrictions;
    private Address address;
    private EventType type;
    private EventPlaceType placeType;
    private EventStatus status;

}
