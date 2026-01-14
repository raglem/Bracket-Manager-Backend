package com.theodenmelgar.bracketmanager.model;

import com.theodenmelgar.bracketmanager.enums.LoginMethodEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class OAuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String oAuthProviderId; // actual id provided by the OAuth client

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private LoginMethodEnum provider = LoginMethodEnum.GOOGLE;

    @OneToOne(mappedBy = "oAuthUser")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getoAuthProviderId() {
        return oAuthProviderId;
    }

    public void setoAuthProviderId(String oAuthProviderId) {
        this.oAuthProviderId = oAuthProviderId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LoginMethodEnum getProvider() {
        return provider;
    }

    public void setProvider(LoginMethodEnum provider) {
        this.provider = provider;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @PrePersist
    @PreUpdate
    private void preventRegularLoginMethod() {
        if (provider == LoginMethodEnum.REGULAR) {
            throw new IllegalStateException(
                    "REGULAR login method is not allowed as a provider for OAuth entity"
            );
        }
    }
}
