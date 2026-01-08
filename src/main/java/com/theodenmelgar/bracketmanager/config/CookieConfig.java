package com.theodenmelgar.bracketmanager.config;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.session.cookie")
public class CookieConfig {
    private boolean httpOnly;
    private boolean secure;
    private String sameSite;

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public String getSameSite() {
        return sameSite;
    }

    public void setSameSite(String sameSite) {
        this.sameSite = sameSite;
    }

    @PostConstruct
    public void validateConfig() {
        // Validation logic: If not secure, sameSite MUST be lax
        if (!secure && !"lax".equalsIgnoreCase(sameSite)) {
            throw new IllegalStateException(
                    "Insecure configuration detected: When 'secure' is false, " +
                            "'same-site' must be set to 'lax' to be accepted by modern browsers."
            );
        }
    }
}
