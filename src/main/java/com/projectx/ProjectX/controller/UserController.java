package com.projectx.ProjectX.controller;

import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.UserRegistrationRequest;
import com.projectx.ProjectX.model.resource.UserResponseResource;
import com.projectx.ProjectX.model.resource.UserUpdateResource;
import com.projectx.ProjectX.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/signup")
    public ResponseEntity<User> registerUser(@RequestBody UserRegistrationRequest request) {
        try {
            User response = userService.register(request);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/test")
    public String test() {
        return "Test";
    }

    @PutMapping()
    public ResponseEntity<Void> updateUserDetails(@RequestBody UserUpdateResource resource,
                                                  @AuthenticationPrincipal User user) {
        try {
            userService.updateUserProfile(user, resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<UserResponseResource> getUserById(@PathVariable Long userId) {
        UserResponseResource response = userService.findUserById(userId);
        return ResponseEntity.ok().body(response);
    }

}
