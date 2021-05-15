package com.projectx.ProjectX.model.resource;

import com.projectx.ProjectX.enums.EstateType;
import com.projectx.ProjectX.model.Address;
import com.projectx.ProjectX.model.Picture;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EstateResponseResource {

    private Long id;
    private Address address;
    private Integer numOfBedrooms;
    private Double area;
    private EstateType type;
    private Double rentPrice;
    private UserResponseResource owner;
    private String description;
    private List<Picture> pictures;

}
