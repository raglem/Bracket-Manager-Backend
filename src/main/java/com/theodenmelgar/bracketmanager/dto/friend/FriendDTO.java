package com.theodenmelgar.bracketmanager.dto.friend;

import com.theodenmelgar.bracketmanager.model.User;

public class FriendDTO {
    private Long id;
    private String username;
    private String profileImageURL;

    public FriendDTO() {}

    public FriendDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
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

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }
}
