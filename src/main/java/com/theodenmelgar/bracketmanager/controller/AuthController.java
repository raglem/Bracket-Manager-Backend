package com.theodenmelgar.bracketmanager.controller;

import com.theodenmelgar.bracketmanager.config.JwtConfig;
import com.theodenmelgar.bracketmanager.dto.auth.AuthDTO;
import com.theodenmelgar.bracketmanager.dto.auth.LoginRequestDTO;
import com.theodenmelgar.bracketmanager.dto.auth.RegisterRequestDTO;
import com.theodenmelgar.bracketmanager.enums.OAuthProviderEnum;
import com.theodenmelgar.bracketmanager.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtConfig jwtConfig;

    public AuthController(AuthService authService, JwtConfig jwtConfig) {
        this.authService = authService;
        this.jwtConfig = jwtConfig;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO authRequestDTO, HttpServletResponse response){
        AuthDTO authDTO = authService.register(authRequestDTO.getUsername(), authRequestDTO.getPassword());

        // Build the cookie and attach it to the response
        String token = authService.generateTokenForUser(authDTO.getUser());
        ResponseCookie cookie = authService.createAuthCookie(token, jwtConfig.getExpiration());

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(authDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO authRequestDTO, HttpServletResponse response ){
        AuthDTO authDTO = authService.login(authRequestDTO.getUsername(), authRequestDTO.getPassword());

        // Build the cookie and attach it to the response
        String token = authService.generateTokenForUser(authDTO.getUser());
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

    @GetMapping("/oauth/success")
    public ResponseEntity<?> oauthSuccess(@AuthenticationPrincipal OAuth2User oAuth2User, HttpServletResponse response, Principal principal) {
        // Extract user info
        String oauthId = oAuth2User.getName();
        String name = oAuth2User.getAttribute("given_name");
        String email = oAuth2User.getAttribute("email");
        // for now, only Google OAuth is supported
        OAuthProviderEnum provider = OAuthProviderEnum.GOOGLE;

        AuthDTO authDTO = authService.oAuthLoginOrRegister(oauthId, name, email, provider);

        // Build the cookie and attach it to the response
        String token = authService.generateTokenForOAuthUser((OAuth2User) principal);
        ResponseCookie cookie = authService.createAuthCookie(token, jwtConfig.getExpiration());

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(authDTO);
    }

    @GetMapping("/me")
    public OAuth2User me(@AuthenticationPrincipal OAuth2User user) {
        return user;
    }
}

