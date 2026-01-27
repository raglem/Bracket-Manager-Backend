package com.theodenmelgar.bracketmanager.repository.friend;

import com.theodenmelgar.bracketmanager.model.Friendship;
import com.theodenmelgar.bracketmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findByUser1(User user1);
    List<Friendship> findByUser2(User user2);
    List<Friendship> findByUser1OrUser2(User user1, User user2);
}
