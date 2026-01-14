package com.theodenmelgar.bracketmanager.dto.auth;

import com.theodenmelgar.bracketmanager.enums.LoginMethodEnum;
import com.theodenmelgar.bracketmanager.model.OAuthUser;

public class OAuthUserDTO {
    private Long id;
    private String oAuthProviderId;
    private String name;
    private String email;
    private String provider;

    public OAuthUserDTO() {}

    public OAuthUserDTO(OAuthUser user) {
        this.id = user.getId();
        this.oAuthProviderId = user.getoAuthProviderId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.provider = user.getProvider().getDisplayName();
    }

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

    public String getProvider() {
        return provider;
    }

    public void setProvider(LoginMethodEnum provider) {
        this.provider = provider.getDisplayName();
    }
}
