package com.theodenmelgar.bracketmanager.dto.friend;

import java.util.List;

public class FriendRequestsDTO {
    private List<SentRequestDTO> sentRequests;
    private List<ReceivedRequestDTO> receivedRequests;

    public FriendRequestsDTO() {}

    public FriendRequestsDTO(
         List<SentRequestDTO> sentRequests, List<ReceivedRequestDTO> receivedRequests) {
        this.sentRequests = sentRequests;
        this.receivedRequests = receivedRequests;
    }

    public List<SentRequestDTO> getSentRequests() {
        return sentRequests;
    }

    public void setSentRequests(List<SentRequestDTO> sentRequests) {
        this.sentRequests = sentRequests;
    }

    public List<ReceivedRequestDTO> getReceivedRequests() {
        return receivedRequests;
    }

    public void setReceivedRequests(List<ReceivedRequestDTO> receivedRequests) {
        this.receivedRequests = receivedRequests;
    }
}
