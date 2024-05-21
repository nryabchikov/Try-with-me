package by.ryabchikov.coursework.service;

import by.ryabchikov.coursework.model.user.Friend;
import by.ryabchikov.coursework.model.user.User;
import by.ryabchikov.coursework.repository.FriendRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FriendService {
    private final FriendRepository friendRepository;


    public List<Friend> getFriends(User currentUser) {
        return friendRepository.findByUser(currentUser);
    }

    public boolean areFriends(User currentUser, User user) {
        List<Friend> friends = friendRepository.findByUser(currentUser);
        if (friends != null) {
            for (Friend friend: friends) {
                if (user.equals(friend.getFriend())) {
                    return true;
                }
            }
        }
        return false;
    }
}
