package com.theodenmelgar.bracketmanager.repository.friend;

import com.theodenmelgar.bracketmanager.model.FriendRequest;
import com.theodenmelgar.bracketmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);
    List<FriendRequest> findBySenderOrReceiver(User sender, User receiver);
    List<FriendRequest> findBySender(User sender);
    List<FriendRequest> findByReceiver(User receiver);
}
