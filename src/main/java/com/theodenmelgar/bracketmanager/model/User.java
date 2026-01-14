package com.theodenmelgar.bracketmanager.model;

import com.theodenmelgar.bracketmanager.enums.LoginMethodEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;
    private String password;

    @NotNull
    private LoginMethodEnum loginMethod = LoginMethodEnum.REGULAR;

    // Add custom fields later

    @OneToOne
    @JoinColumn(name = "oauthUser_id", updatable = false)
    private OAuthUser oAuthUser;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginMethodEnum getLoginMethod() {
        return loginMethod;
    }

    public void setLoginMethod(LoginMethodEnum loginMethod) {
        this.loginMethod = loginMethod;
    }

    public OAuthUser getOAuthUser() {
        return oAuthUser;
    }

    public void setOAuthUser(OAuthUser oAuthUser) {
        this.oAuthUser = oAuthUser;
    }
}
