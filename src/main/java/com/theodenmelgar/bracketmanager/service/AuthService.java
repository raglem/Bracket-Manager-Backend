package com.theodenmelgar.bracketmanager.service;

import com.theodenmelgar.bracketmanager.config.CookieConfig;
import com.theodenmelgar.bracketmanager.dto.auth.*;
import com.theodenmelgar.bracketmanager.enums.LoginMethodEnum;
import com.theodenmelgar.bracketmanager.exception.user.*;
import com.theodenmelgar.bracketmanager.model.OAuthUser;
import com.theodenmelgar.bracketmanager.model.User;
import com.theodenmelgar.bracketmanager.repository.OAuthUserRepository;
import com.theodenmelgar.bracketmanager.repository.UserRepository;
import com.theodenmelgar.bracketmanager.security.JwtProvider;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final OAuthUserRepository oAuthUserRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CookieConfig cookieConfig;
    private final UserService userService;

    public AuthService(
            OAuthUserRepository oAuthUserRepository, UserRepository userRepository,
            PasswordEncoder passwordEncoder, JwtProvider jwtProvider,
            CookieConfig cookieConfig, UserService userService
    ) {
        this.oAuthUserRepository = oAuthUserRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.cookieConfig = cookieConfig;
        this.userService = userService;
    }

    public AuthDTO register(String username, String password) {

        // Check if the email has been taken
        if (userRepository.findByUsername(username).isPresent()){
            throw new UsernameAlreadyTakenException(username);
        }
        else {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setLoginMethod(LoginMethodEnum.REGULAR);

            // Hash password
            newUser.setPassword(passwordEncoder.encode(password));
            userRepository.save(newUser);

            UserDTO newUserDTO = userService.constructUserDTO(newUser);
            return new AuthDTO("Register", newUserDTO, null);
        }
    }

    public AuthDTO login(String username, String password) {

        // Authenticate the user
        User user = userRepository.findByUsername(username)
                .orElseThrow(InvalidUserCredentialsException::new);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidUserCredentialsException();
        }

        // Return the AuthDTO with the user's current details
        UserDTO userDTO = userService.constructUserDTO(user);
        return new AuthDTO("Login", userDTO, null);
    }

    public AuthDTO oAuthLoginOrRegister(String oauthId, String name, String email, LoginMethodEnum provider){

        // Check if there is a corresponding existing OAuthUser
        if (oAuthUserRepository.findByProviderAndOAuthProviderId(provider, oauthId).isPresent()) {
            OAuthUser oAuthUser = oAuthUserRepository.findByProviderAndOAuthProviderId(provider, oauthId).get();
            User user = oAuthUser.getUser();

            // Construct DTOs for response
            UserDTO userDTO = userService.constructUserDTO(user);
            OAuthUserDTO oAuthDTO = new OAuthUserDTO(oAuthUser);
            return new AuthDTO("Login", userDTO, oAuthDTO);
        }
        // If there is no OAuthUser, we handle registration
        // A new OAuthUser and User entity must be created
        else {
            OAuthUser oAuthUser = new OAuthUser();
            oAuthUser.setoAuthProviderId(oauthId);
            oAuthUser.setName(name);
            oAuthUser.setEmail(email);
            oAuthUserRepository.save(oAuthUser);

            User user = new User();
            String uniqueUsername = name;

            // Username must be unique
            while(userRepository.findByUsername(uniqueUsername).isPresent()){
                uniqueUsername += (int)(Math.random() * 10);
            }
            user.setUsername(uniqueUsername);
            user.setEmail(email);
            user.setOAuthUser(oAuthUser);
            userRepository.save(user);

            // Construct DTOs for response
            UserDTO userDTO = userService.constructUserDTO(user);
            OAuthUserDTO oAuthDTO = new OAuthUserDTO(oAuthUser);
            return new AuthDTO("Login", userDTO, oAuthDTO);
        }
    }

    public void resetPassword(Long userId, PasswordResetRequestDTO resetDTO){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // Verify the user with their old password before proceeding
        if (!passwordEncoder.matches(resetDTO.getOldPassword(), user.getPassword())) {
            throw new WrongPasswordException();
        }

        user.setPassword(passwordEncoder.encode(resetDTO.getNewPassword()));
        userRepository.save(user);
    }

    public void editUser(Long userId, EditUserRequestDTO editUserDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        String username = editUserDTO.getUsername();
        String email = editUserDTO.getEmail();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyTakenException(username);
        }
        if (!email.isEmpty() && userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyTakenException(email);
        }

        user.setUsername(editUserDTO.getUsername());
        user.setEmail(editUserDTO.getEmail());

        userRepository.save(user);
    }

    public String generateTokenForUser(Long userId) {
        return jwtProvider.createToken(userId);
    }

    public ResponseCookie createAuthCookie(String value, long maxAge){
        return ResponseCookie.from("AUTH-TOKEN", value)
                .httpOnly(cookieConfig.isHttpOnly())
                .secure(cookieConfig.isSecure())
                .path("/")
                .sameSite(cookieConfig.getSameSite())
                .maxAge(maxAge)
                .build();
    }
}
