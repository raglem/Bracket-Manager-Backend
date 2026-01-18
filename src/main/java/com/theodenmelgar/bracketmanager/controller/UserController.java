package com.theodenmelgar.bracketmanager.controller;

import com.theodenmelgar.bracketmanager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/profile-image")
    public ResponseEntity<?> uploadProfileImage(
            @RequestParam("image") MultipartFile image,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Long userId = Long.parseLong(userDetails.getUsername());
        String imageUrl = userService.changeProfileImage(userId, image);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                "message", "Profile image upload successful",
                "profileImageURL", imageUrl));

    }

    @DeleteMapping("/profile-image")
    public ResponseEntity<?> uploadProfileImage(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        userService.removeProfileImage(userId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(Map.of(
                "message", "Profile image deletion successful"));
    }
}
