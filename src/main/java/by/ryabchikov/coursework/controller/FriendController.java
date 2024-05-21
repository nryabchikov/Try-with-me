package by.ryabchikov.coursework.controller;

import by.ryabchikov.coursework.model.user.Friend;
import by.ryabchikov.coursework.model.user.FriendRequest;
import by.ryabchikov.coursework.model.user.User;
import by.ryabchikov.coursework.service.FriendRequestService;
import by.ryabchikov.coursework.service.FriendService;
import by.ryabchikov.coursework.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class FriendController {
    private final UserService userService;
    private final FriendRequestService friendRequestService;
    private final FriendService friendService;

    @GetMapping("/addFriend/{userLogin}")
    public String sendFriendRequest(@PathVariable String userLogin) {
        User currentUser = userService.getCurrentUser();
        User receiver = userService.getUserByLogin(userLogin);
        friendRequestService.sendFriendRequest(currentUser, receiver);
        return "redirect:/profile";
    }

    @GetMapping("/friends")
    public String getFriendsPage(Model model) {
        User currentUser = userService.getCurrentUser();
        List<FriendRequest> friendRequests = friendRequestService.getFriendRequestsForUser(currentUser);
        model.addAttribute("friendRequests", friendRequests);

        List<Friend> friendList = friendService.getFriends(currentUser);
        model.addAttribute("friends", friendList);
        return "friends";
    }

    @PostMapping("/acceptFriendRequest")
    public String acceptFriendRequest(@RequestParam("requestId") Long requestId) {
        FriendRequest friendRequest = friendRequestService.getFriendRequestById(requestId);
        friendRequestService.acceptFriendRequest(friendRequest);
        return "redirect:/profile";
    }

    @PostMapping("rejectFriendRequest")
    public String rejectFriendRequest(@RequestParam("requestId") Long requestId) {
        FriendRequest friendRequest = friendRequestService.getFriendRequestById(requestId);
        friendRequestService.rejectFriendRequest(friendRequest);
        return "redirect:/profile";
    }
}
