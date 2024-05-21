package by.ryabchikov.coursework.service;

import by.ryabchikov.coursework.model.user.Friend;
import by.ryabchikov.coursework.model.user.FriendRequest;
import by.ryabchikov.coursework.model.user.User;
import by.ryabchikov.coursework.repository.FriendRepository;
import by.ryabchikov.coursework.repository.FriendRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Service
public class FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;
    private final FriendRepository friendRepository;

    public void sendFriendRequest(User sender, User receiver) {
        FriendRequest request = new FriendRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        friendRequestRepository.save(request);
    }

    public void acceptFriendRequest(FriendRequest request) {
        Friend friend1 = new Friend();
        friend1.setUser(request.getReceiver());
        friend1.setFriend(request.getSender());
        friendRepository.save(friend1);

        Friend friend2 = new Friend();
        friend2.setUser(request.getSender());
        friend2.setFriend(request.getReceiver());
        friendRepository.save(friend2);
        friendRequestRepository.delete(request);
    }

    public void rejectFriendRequest(FriendRequest request) {
        friendRequestRepository.delete(request);
    }

    public List<FriendRequest> getFriendRequestsForUser(User currentUser) {
        return friendRequestRepository.findByReceiver(currentUser);
    }

//    public boolean hasFriendRequestFromUser(User currentUser, User user) {
//        return Objects.equals(currentUser.getId(), friendRequestRepository.findBySender(user).getFirst().getReceiver().getId());
//    }
    public boolean hasFriendRequestFromUser(User currentUser, User user) {
        List<FriendRequest> friendRequests = friendRequestRepository.findBySender(user);
        if (friendRequests != null && !friendRequests.isEmpty()) {
            for (FriendRequest request: friendRequests) {
                if (Objects.equals(currentUser.getId(), request.getReceiver().getId())) {
                    return true;
                }
            }
        }
        return false;
    }


//    public FriendRequest getFriendRequestById(Long requestId) {
//        return friendRequestRepository.findById(requestId).orElse(null);
//    }

    public FriendRequest getFriendRequestById(Long requestId) {
        return friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));
    }

    public Long getFriendRequestIdFromUser(User user, User currentUser) {
        return friendRequestRepository.findBySenderAndReceiver(user, currentUser).getId();
    }

    public boolean hasSendRequestToUser(User currentUser, User user) {
        List<FriendRequest> requestList = friendRequestRepository.findBySender(currentUser);
        for (FriendRequest request: requestList) {
            if (Objects.equals(request.getReceiver(), user)) {
                return true;
            }
        }
        return false;
    }
}
