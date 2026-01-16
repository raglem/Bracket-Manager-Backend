package com.theodenmelgar.bracketmanager.controller;

import com.theodenmelgar.bracketmanager.service.UserService;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/{id}/profile-image")
    public ResponseEntity<?> uploadProfileImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile image
    ) {

        String imageUrl = userService.changeProfileImage(id, image);
        return ResponseEntity.ok(Map.of(
                "message", "Profile image upload successful",
                "imageUrl", imageUrl));

    }
}
