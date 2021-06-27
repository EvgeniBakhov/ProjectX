package com.projectx.ProjectX.model.resource;

import com.projectx.ProjectX.model.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateResource {

    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private String phone;
    private Address address;

}
