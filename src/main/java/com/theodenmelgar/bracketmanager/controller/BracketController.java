package com.theodenmelgar.bracketmanager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bracket")
public class BracketController {
    @GetMapping
    public String testBracketEndpoint() {
        return "Hello! This is the bracket endpoint!!!";
    }
}
