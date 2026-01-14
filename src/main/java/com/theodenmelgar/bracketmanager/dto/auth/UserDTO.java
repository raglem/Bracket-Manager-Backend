package com.theodenmelgar.bracketmanager.dto.auth;

import com.theodenmelgar.bracketmanager.enums.LoginMethodEnum;
import com.theodenmelgar.bracketmanager.model.User;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String loginMethod;

    public UserDTO(){}

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.loginMethod = user.getLoginMethod().getDisplayName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getLoginMethod() {
        return loginMethod;
    }

    public void setLoginMethod(String loginMethod) {
        this.loginMethod = loginMethod;
    }
}
