package com.theodenmelgar.bracketmanager.controller;

import com.theodenmelgar.bracketmanager.config.JwtConfig;
import com.theodenmelgar.bracketmanager.dto.auth.*;
import com.theodenmelgar.bracketmanager.exception.ErrorResponse;
import com.theodenmelgar.bracketmanager.exception.user.*;
import com.theodenmelgar.bracketmanager.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtConfig jwtConfig;

    public AuthController(
            AuthService authService, JwtConfig jwtConfig
    ) {
        this.authService = authService;
        this.jwtConfig = jwtConfig;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO authRequestDTO, HttpServletResponse response){
        AuthDTO authDTO = authService.register(authRequestDTO.getUsername(), authRequestDTO.getPassword());

        // Build the cookie and attach it to the response
        String token = authService.generateTokenForUser(authDTO.getUser().getId());
        ResponseCookie cookie = authService.createAuthCookie(token, jwtConfig.getExpiration());

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(authDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO authRequestDTO, HttpServletResponse response ){
        AuthDTO authDTO = authService.login(authRequestDTO.getUsername(), authRequestDTO.getPassword());

        // Build the cookie and attach it to the response
        String token = authService.generateTokenForUser(authDTO.getUser().getId());
        ResponseCookie cookie = authService.createAuthCookie(token, jwtConfig.getExpiration());

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(authDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response){
        ResponseCookie cookie = authService.createAuthCookie("", 0);

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequestDTO resetDTO,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        authService.resetPassword(userId, resetDTO);
        return ResponseEntity.ok("Password successfully reset");
    }

    @PutMapping("/edit-user")
    public ResponseEntity<String> editUser(@RequestBody EditUserRequestDTO editUserDTO,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        authService.editUser(userId, editUserDTO);
        return ResponseEntity.ok("User details successfully updated");
    }

    @GetMapping("/me")
    public OAuth2User me(@AuthenticationPrincipal OAuth2User user) {
        return user;
    }

    @ExceptionHandler(value = InvalidUserCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidUserCredentialsException(InvalidUserCredentialsException ex) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
    }

    @ExceptionHandler(value = UsernameAlreadyTakenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUsernameAlreadyTakenException(UsernameAlreadyTakenException ex) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

    @ExceptionHandler(value = EmailAlreadyTakenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEmailAlreadyTakenException(EmailAlreadyTakenException ex) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException ex){
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(value = WrongPasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleWrongPasswordException(WrongPasswordException ex){
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
    }
}

