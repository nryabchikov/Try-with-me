package by.ryabchikov.coursework.repository;

import by.ryabchikov.coursework.model.user.FriendRequest;
import by.ryabchikov.coursework.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByReceiver(User receiver);
    List<FriendRequest> findBySender(User sender);
    FriendRequest findBySenderAndReceiver(User sender, User receiver);
}
