package com.theodenmelgar.bracketmanager.model;

import com.theodenmelgar.bracketmanager.enums.OAuthProviderEnum;
import jakarta.persistence.*;
import org.springframework.web.bind.annotation.Mapping;

@Entity
public class OAuthUser {
    @Id
    private String id; // actual id provided by the OAuth client

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private OAuthProviderEnum provider = OAuthProviderEnum.GOOGLE;

    @OneToOne(mappedBy = "oAuthUser")
    private User user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public OAuthProviderEnum getProvider() {
        return provider;
    }

    public void setProvider(OAuthProviderEnum provider) {
        this.provider = provider;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
