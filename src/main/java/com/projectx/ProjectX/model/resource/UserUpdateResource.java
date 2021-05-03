package com.projectx.ProjectX.model.resource;

import com.projectx.ProjectX.model.Address;
import com.projectx.ProjectX.model.Picture;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateResource {

    private String phone;
    private Picture picture;
    private Address address;

}
