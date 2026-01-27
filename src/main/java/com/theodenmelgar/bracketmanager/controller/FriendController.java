package com.theodenmelgar.bracketmanager.controller;

import com.theodenmelgar.bracketmanager.dto.friend.SentRequestDTO;
import com.theodenmelgar.bracketmanager.exception.ErrorResponse;
import com.theodenmelgar.bracketmanager.exception.friend.FriendRequestAlreadySentException;
import com.theodenmelgar.bracketmanager.exception.friend.FriendRequestForbiddenException;
import com.theodenmelgar.bracketmanager.exception.user.UserNotFoundException;
import com.theodenmelgar.bracketmanager.service.FriendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friend")
public class FriendController {

    private final FriendService friendService;
    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping
    public ResponseEntity<?> getFriends(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(friendService.getFriends(userId));
    }

    @GetMapping
    @RequestMapping("/search")
    public ResponseEntity<?> searchForFriends(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestParam String username) {
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(friendService.searchUsersByUsername(userId, username));
    }

    @GetMapping
    @RequestMapping("/requests")
    public ResponseEntity<?> getFriendRequests(@AuthenticationPrincipal UserDetails currentUser) {
        Long userId = Long.parseLong(currentUser.getUsername());
        return ResponseEntity.ok(friendService.getFriendRequests(userId));
    }

    @PostMapping("/send/{receiverId}")
    public ResponseEntity<?> sendFriendRequest(@PathVariable Long receiverId,
                                    @AuthenticationPrincipal UserDetails userDetails)
    {
        Long userId = Long.parseLong(userDetails.getUsername());
        SentRequestDTO sentRequest = friendService.sendFriendRequest(userId, receiverId);
        return ResponseEntity.ok(sentRequest);
    }

    @PostMapping("/accept/{friendRequestId}")
    public String acceptFriendRequest(@PathVariable Long friendRequestId,
                                      @AuthenticationPrincipal UserDetails userDetails)
    {
        Long userId = Long.parseLong(userDetails.getUsername());
        friendService.acceptFriendRequest(friendRequestId, userId);
        return "Friend request successfully accepted";
    }

    @DeleteMapping("/reject/{friendRequestId}")
    public String rejectFriendRequest(@PathVariable Long friendRequestId, @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        friendService.rejectFriendRequest(friendRequestId, userId);
        return "Friend request successfully rejected";
    }

    @DeleteMapping("/cancel/{friendRequestId}")
    public String cancelFriendRequest(@PathVariable Long friendRequestId, @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        friendService.cancelFriendRequest(friendRequestId, userId);
        return "Friend request successfully canceled";
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException ex){
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(value = FriendRequestAlreadySentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleFriendRequestAlreadySentException(FriendRequestAlreadySentException ex){
        return new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

    @ExceptionHandler(value = FriendRequestForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleFriendRequestForbiddenException(FriendRequestForbiddenException ex){
        return new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
    }
}
