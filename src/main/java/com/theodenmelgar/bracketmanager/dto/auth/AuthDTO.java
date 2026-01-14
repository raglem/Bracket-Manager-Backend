package com.theodenmelgar.bracketmanager.dto.auth;

import com.theodenmelgar.bracketmanager.model.OAuthUser;
import com.theodenmelgar.bracketmanager.model.User;

public class AuthDTO {
    private String authAction; // "Login" or "Register"
    private UserDTO user;
    private OAuthUserDTO oAuthUser;

    public AuthDTO() {}

    public AuthDTO(String authAction, User user, OAuthUser oAuthUser) {
        this.authAction = authAction;
        this.user = new UserDTO(user);
        if(oAuthUser != null) {
            this.oAuthUser = new OAuthUserDTO(oAuthUser);
        }
    }

    public String getAuthAction() {
        return authAction;
    }

    public void setAuthAction(String authAction) {
        this.authAction = authAction;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public OAuthUserDTO getoAuthUser() {
        return oAuthUser;
    }

    public void setoAuthUser(OAuthUserDTO oAuthUser) {
        this.oAuthUser = oAuthUser;
    }
}
