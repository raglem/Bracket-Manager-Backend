package com.theodenmelgar.bracketmanager.service;

import com.theodenmelgar.bracketmanager.dto.friend.FriendDTO;
import com.theodenmelgar.bracketmanager.dto.friend.FriendRequestsDTO;
import com.theodenmelgar.bracketmanager.dto.friend.ReceivedRequestDTO;
import com.theodenmelgar.bracketmanager.dto.friend.SentRequestDTO;
import com.theodenmelgar.bracketmanager.exception.BadRequestException;
import com.theodenmelgar.bracketmanager.exception.friend.FriendRequestAlreadySentException;
import com.theodenmelgar.bracketmanager.exception.friend.FriendRequestForbiddenException;
import com.theodenmelgar.bracketmanager.exception.user.UserNotFoundException;
import com.theodenmelgar.bracketmanager.model.FriendRequest;
import com.theodenmelgar.bracketmanager.model.Friendship;
import com.theodenmelgar.bracketmanager.model.User;
import com.theodenmelgar.bracketmanager.repository.UserRepository;
import com.theodenmelgar.bracketmanager.repository.friend.FriendRequestRepository;
import com.theodenmelgar.bracketmanager.repository.friend.FriendshipRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FriendService {

    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public FriendService(
            FriendRequestRepository friendRequestRepository, FriendshipRepository friendshipRepository,
            UserRepository userRepository, UserService userService) {
        this.friendRequestRepository = friendRequestRepository;
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public List<FriendDTO> getFriends(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        List<FriendDTO> friends = new ArrayList<>();

        friends.addAll(friendshipRepository.findByUser1(user)
                .stream()
                .map(friend -> new FriendDTO(friend.getUser2()))
                .toList()
        );

        friends.addAll(friendshipRepository.findByUser2(user)
                .stream()
                .map(friend -> new FriendDTO(friend.getUser1()))
                .toList()
        );

        friends = friends
                .stream()
                .sorted(Comparator.comparing(FriendDTO::getUsername))
                .toList();

        return friends;
    }

    public List<FriendDTO> searchUsersByUsername(Long currentUserId, String username) {
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Set<Long> pendingRequestIds = friendRequestRepository
                .findBySenderOrReceiver(user, user)
                .stream()
                .map(friendRequest -> {
                    if (friendRequest.getSender().equals(user)) {
                        return friendRequest.getReceiver().getId();
                    }
                    else{
                        return friendRequest.getSender().getId();
                    }
                })
                .collect(Collectors.toSet());

        Set<Long> friendIds = friendshipRepository
                .findByUser1OrUser2(user, user)
                .stream()
                .flatMap(friendship -> Stream.of(friendship.getUser1().getId(), friendship.getUser2().getId()))
                .filter(id -> !Objects.equals(id, currentUserId))
                .collect(Collectors.toSet());

        return userRepository.findTop10ByUsernameContainingIgnoreCase(username)
                .stream()
                .filter(matchingUser -> {
                    if (Objects.equals(user.getId(), matchingUser.getId())) return false;
                    return !friendIds.contains(matchingUser.getId())
                            && !pendingRequestIds.contains(matchingUser.getId());
                })
                .map(FriendDTO::new)
                .toList();
    }

    public SentRequestDTO sendFriendRequest(Long senderId, Long receiverId) {
        if (senderId.equals(receiverId)) {
            throw new BadRequestException("Users cannot send friend requests to themselves");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new UserNotFoundException(senderId));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new UserNotFoundException(receiverId));

        if (friendRequestRepository.findBySenderAndReceiver(sender, receiver).isPresent()) {
            throw new FriendRequestAlreadySentException(sender, receiver);
        }
        if (friendRequestRepository.findBySenderAndReceiver(receiver, sender).isPresent()) {
            throw new FriendRequestAlreadySentException(receiver, sender);
        }

        FriendRequest friendRequest = new FriendRequest(sender, receiver);
        friendRequestRepository.save(friendRequest);
        return new SentRequestDTO(friendRequest.getId(), new FriendDTO(friendRequest.getReceiver()));
    }

    public void acceptFriendRequest(Long friendRequestId, Long currentUserId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException(currentUserId));

        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId)
                .orElseThrow(() -> new FriendRequestForbiddenException(currentUser));

        User user1 = friendRequest.getSender().getId() < friendRequest.getReceiver().getId() ?
                friendRequest.getSender() : friendRequest.getReceiver();
        User user2 = friendRequest.getSender().getId() < friendRequest.getReceiver().getId() ?
                friendRequest.getReceiver() : friendRequest.getSender();

        friendRequestRepository.delete(friendRequest);
        Friendship friendship = new Friendship(user1, user2);
        friendshipRepository.save(friendship);
    }

    public void rejectFriendRequest(Long friendRequestId, Long currentUserId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException(currentUserId));

        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId)
                .orElseThrow(() -> new FriendRequestForbiddenException(currentUser));

        if (!currentUserId.equals(friendRequest.getReceiver().getId())) {
            throw new FriendRequestForbiddenException(currentUser);
        }

        friendRequestRepository.deleteById(friendRequestId);
    }

    public void cancelFriendRequest(Long friendRequestId, Long currentUserId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException(currentUserId));

        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId)
                .orElseThrow(() -> new FriendRequestForbiddenException(currentUser));

        if (!currentUserId.equals(friendRequest.getSender().getId())) {
            throw new FriendRequestForbiddenException(currentUser);
        }

        friendRequestRepository.deleteById(friendRequestId);
    }

    public FriendRequestsDTO getFriendRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        List<FriendRequest> sentRequests = friendRequestRepository.findBySender(user)
                .stream()
                .sorted(Comparator.comparing(FriendRequest::getSentAt).reversed())
                .toList();
        List<FriendRequest> receivedRequests = friendRequestRepository.findByReceiver(user)
                .stream()
                .sorted(Comparator.comparing(FriendRequest::getSentAt).reversed())
                .toList();

        List<SentRequestDTO> sentRequestsDTO =
                sentRequests.stream()
                        .map(friendRequest -> {
                            FriendDTO receiver = new FriendDTO(friendRequest.getReceiver());
                            if (receiver.getProfileImageURL() != null && !receiver.getProfileImageURL().isEmpty()) {
                                receiver.setProfileImageURL(
                                        userService.getImageURL(friendRequest.getReceiver())
                                );
                            }

                            return new SentRequestDTO(
                                    friendRequest.getId(),
                                    receiver
                            );
                        })
                        .toList();

        List<ReceivedRequestDTO> receivedRequestDTO =
                receivedRequests.stream()
                        .map(friendRequest -> {
                            FriendDTO sender = new FriendDTO(friendRequest.getSender());
                            if (sender.getProfileImageURL() != null && !sender.getProfileImageURL().isEmpty()) {
                                sender.setProfileImageURL(
                                        userService.getImageURL(friendRequest.getSender())
                                );
                            }

                            return new ReceivedRequestDTO(
                                    friendRequest.getId(),
                                    sender
                            );
                        })
                .toList();

        return new FriendRequestsDTO(sentRequestsDTO, receivedRequestDTO);
    }
}
