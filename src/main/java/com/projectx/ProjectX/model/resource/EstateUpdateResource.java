package com.projectx.ProjectX.model.resource;

import com.projectx.ProjectX.enums.EstateType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstateUpdateResource {

    private Integer numOfBeds;
    private Double area;
    private EstateType type;
    private Double rentPrice;
    private String description;
}
