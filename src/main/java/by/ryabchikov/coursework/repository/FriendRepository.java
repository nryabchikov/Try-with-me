package by.ryabchikov.coursework.repository;

import by.ryabchikov.coursework.model.user.Friend;
import by.ryabchikov.coursework.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findByUser(User user);
}
