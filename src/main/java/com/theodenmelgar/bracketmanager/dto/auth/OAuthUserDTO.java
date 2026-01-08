package com.theodenmelgar.bracketmanager.dto.auth;

import com.theodenmelgar.bracketmanager.enums.OAuthProviderEnum;
import com.theodenmelgar.bracketmanager.model.OAuthUser;

public class OAuthUserDTO {
    private Long id;
    private String oAuthID;
    private String name;
    private String email;
    private OAuthProviderEnum provider;

    public OAuthUserDTO() {}

    public OAuthUserDTO(OAuthUser user) {
        this.id = user.getId();
        this.oAuthID = user.getoAuthId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.provider = user.getProvider();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getoAuthID() {
        return oAuthID;
    }

    public void setoAuthID(String oAuthID) {
        this.oAuthID = oAuthID;
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
}
