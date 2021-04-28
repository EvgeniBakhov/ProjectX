package com.projectx.ProjectX.controller;

import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping(value = "/register")
    public void registerUser(@RequestBody User user) {
        userService.register(user);
    }

    @GetMapping(value = "/test")
    public String test() {
        return "Hey, it's test of a good ass site. Get out of here! Click here: https://www.youtube.com/watch?v=WPkMUU9tUqk";
    }
}
