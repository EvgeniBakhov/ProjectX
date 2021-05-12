package com.projectx.ProjectX.model.resource;

import com.projectx.ProjectX.enums.EsrbRestrictions;
import com.projectx.ProjectX.enums.EventPlaceType;
import com.projectx.ProjectX.enums.EventType;
import com.projectx.ProjectX.model.Address;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EventCreateRequest {

    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private int capacity;
    private EsrbRestrictions ageRestrictions;
    private Address address;
    private EventType type;
    private EventPlaceType placeType;

}
