package com.projectx.ProjectX.model.resource;

import com.projectx.ProjectX.enums.EstateType;
import com.projectx.ProjectX.model.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstateCreateRequest {
    private Address address;
    private Integer numOfBedrooms;
    private Double area;
    private EstateType type;
    private Double rentPrice;
    private String description;
}
