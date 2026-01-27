package com.theodenmelgar.bracketmanager.dto.friend;

public class ReceivedRequestDTO {
    private Long id;
    private FriendDTO sender;

    public ReceivedRequestDTO() {}

    public ReceivedRequestDTO(Long id, FriendDTO sender) {
        this.id = id;
        this.sender = sender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FriendDTO getSender() {
        return sender;
    }

    public void setSender(FriendDTO sender) {
        this.sender = sender;
    }
}
