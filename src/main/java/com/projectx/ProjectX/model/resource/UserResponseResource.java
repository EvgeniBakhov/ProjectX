package com.projectx.ProjectX.model.resource;

import com.projectx.ProjectX.model.Address;
import com.projectx.ProjectX.enums.UserType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseResource {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private String phone;
    private UserType type;
    private String picture;
    private Address address;

}
