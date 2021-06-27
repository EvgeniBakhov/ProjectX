package com.projectx.ProjectX.controller;

import com.projectx.ProjectX.exceptions.EntityNotFoundException;
import com.projectx.ProjectX.model.GenericError;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.UserRegistrationRequest;
import com.projectx.ProjectX.model.resource.UserResponseResource;
import com.projectx.ProjectX.model.resource.UserUpdateResource;
import com.projectx.ProjectX.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping(value = "/signup")
    @ApiOperation(
            value = "Register user",
            notes = "Creates new user and persists it in the db")
    public ResponseEntity registerUser(@RequestBody UserRegistrationRequest request) {
        try {
            UserResponseResource response = userService.register(request);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new GenericError(new Date(), e.getMessage(), ""));
        }
    }

    @PutMapping()
    public ResponseEntity updateUserDetails(@RequestBody UserUpdateResource resource,
                                                  @AuthenticationPrincipal User user) {
        UserResponseResource updatedUser = null;
        try {
           updatedUser = userService.updateUserProfile(user, resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new GenericError(new Date(), e.getMessage(), ""));
        }
        return ResponseEntity.ok().body(updatedUser);
    }

    @PreAuthorize("hasAuthority('DELETE_USER')")
    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        if (!userService.deleteUser(userId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity getUserById(@PathVariable Long userId) {
        UserResponseResource response = userService.findUserById(userId);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new GenericError(new Date(), "User is not found.", "Does not exist."));
        }
        return ResponseEntity.ok().body(response);
    }

    @PreAuthorize("hasAuthority('EDIT_USER')")
    @PutMapping(value = "/{userId}")
    public ResponseEntity<Void> editUserById(@PathVariable Long userId, @RequestBody UserUpdateResource resource) {
        try {
            userService.editUser(userId, resource);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }


    @PostMapping(value = "/picture")
    public ResponseEntity<Void> addPicture(@AuthenticationPrincipal User user,
                                           @RequestParam("picture") MultipartFile picture) {
        if (userService.uploadPicture(picture, user)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value ="/authenticated")
    public ResponseEntity getAuthUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "{userId}/picture", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getPicture(@PathVariable Long userId) {
        Path url = null;
        try {
            url = this.userService.getPictureUrl(userId);
        } catch (EntityNotFoundException e) {
            return null;
        }
        try {
            return Files.readAllBytes(url);
        } catch (Exception e) {
            return null;
        }
    }

//    @PostMapping("/resetPassword")
//    public ResponseEntity<Void> resetPassword(HttpServletRequest request, @RequestParam("email") String email) throws UserNotFoundException {
//        User user = userService.findUserByEmail(email);
//        if (user == null) {
//            throw new UserNotFoundException("User with this email not found.");
//        }
//        String token = UUID.randomUUID().toString();
//        userService.createPasswordResetTokenForUser(user, token);
//        mailSender.send(constructResetTokenEmail("localhost:8080",
//                request.getLocale(), token, user));
//        return ResponseEntity.ok().build();
//    }
//
//    private SimpleMailMessage constructResetTokenEmail(
//            String contextPath, Locale locale, String token, User user) {
//        String url = contextPath + "/user/changePassword?token=" + token;
//        String message = messages.getMessage("message.resetPassword",
//                null, locale);
//        return constructEmail("Reset Password", message + " \r\n" + url, user);
//    }
//
//    private SimpleMailMessage constructEmail(String subject, String body,
//                                             User user) {
//        SimpleMailMessage email = new SimpleMailMessage();
//        email.setSubject(subject);
//        email.setText(body);
//        email.setTo(user.getEmail());
//        email.setFrom(env.getProperty("support.email"));
//        return email;
//    }
}
