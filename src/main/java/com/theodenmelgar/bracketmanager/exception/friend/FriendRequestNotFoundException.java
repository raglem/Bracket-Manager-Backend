package com.theodenmelgar.bracketmanager.exception.friend;

public class FriendRequestNotFoundException extends RuntimeException {
    public FriendRequestNotFoundException(Long id) {
        super("Could not find friend request with id " + id);
    }
}
