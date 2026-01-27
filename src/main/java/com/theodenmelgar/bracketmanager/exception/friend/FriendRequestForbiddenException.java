package com.theodenmelgar.bracketmanager.exception.friend;

import com.theodenmelgar.bracketmanager.model.User;

public class FriendRequestForbiddenException extends RuntimeException {
    public FriendRequestForbiddenException(User user) {
        super("User " + user.getUsername() + " is not authorized to perform this action on this friend request");
    }
}
