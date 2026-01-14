package com.theodenmelgar.bracketmanager.enums;

public enum LoginMethodEnum {
    REGULAR("Regular"),
    GOOGLE("Google"); // add more in the future if necessary

    private final String displayName;

    LoginMethodEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
