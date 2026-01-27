package com.theodenmelgar.bracketmanager.dto.friend;

public class SentRequestDTO {
    private Long id;
    private FriendDTO receiver;

    public SentRequestDTO() {}

    public SentRequestDTO(Long id, FriendDTO receiver) {
        this.id = id;
        this.receiver = receiver;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FriendDTO getReceiver() {
        return receiver;
    }

    public void setReceiver(FriendDTO receiver) {
        this.receiver = receiver;
    }
}
