package com.theodenmelgar.bracketmanager.exception.friend;

import com.theodenmelgar.bracketmanager.model.FriendRequest;
import com.theodenmelgar.bracketmanager.model.User;

public class FriendRequestAlreadySentException extends RuntimeException {
    public FriendRequestAlreadySentException(User sender, User receiver) {
        super("Friend request already exists for sender " + sender.getUsername() + " and receiver " + receiver.getUsername());
    }
}
