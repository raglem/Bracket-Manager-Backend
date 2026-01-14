package com.theodenmelgar.bracketmanager.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EditUserRequestDTO {
    @Size(min = 3, max = 16, message = "Username must be between 3 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*-_+.]+$", message = "Username can only contain letters, numbers, dots, and underscores")
    private String username;

    @Email(message = "Please provide a valid email address")
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
