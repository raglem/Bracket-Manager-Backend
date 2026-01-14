package com.theodenmelgar.bracketmanager.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bracket")
public class BracketController {
    @GetMapping
    public String testBracketEndpoint(@AuthenticationPrincipal UserDetails userDetails) {
        Long id = Long.parseLong(userDetails.getUsername());
        return "Hello! This is the bracket endpoint!!! Your id is " + id;
    }
}
