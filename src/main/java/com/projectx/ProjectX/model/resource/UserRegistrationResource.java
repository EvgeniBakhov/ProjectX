package com.projectx.ProjectX.model.resource;

import com.projectx.ProjectX.model.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationResource {

    private String username;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private String password;
    private String phone;
    private Address address;
}
