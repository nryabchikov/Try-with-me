package by.ryabchikov.coursework.controller;

import by.ryabchikov.coursework.config.MyUserDetails;
import by.ryabchikov.coursework.dto.user.AnotherUserProfile;
import by.ryabchikov.coursework.mapper.UserMapper;
import by.ryabchikov.coursework.model.user.User;
import by.ryabchikov.coursework.service.FriendRequestService;
import by.ryabchikov.coursework.service.FriendService;
import by.ryabchikov.coursework.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class FeedController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final FriendRequestService friendRequestService;
    private final FriendService friendService;
    private final SessionRegistry sessionRegistry;
    
    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @ResponseBody
    @GetMapping("/search")
    public List<User> searchUsers(@RequestParam(name = "query") String query) {
        return userService.findUser(query);
    }

    @GetMapping("/profile/{login}")
    public String getUserProfile(@PathVariable String login, Model model) {
        User user = userService.getUserByLogin(login);
        User currentUser = userService.getCurrentUser();
        boolean hasFriendRequest = friendRequestService.hasFriendRequestFromUser(currentUser, user);
        boolean hasSendRequestToUser = friendRequestService.hasSendRequestToUser(currentUser, user);
        boolean isFriend = friendService.areFriends(currentUser, user);
        //System.out.println(hasFriendRequest);
        if (currentUser.getLogin().equals(login)) {
            model.addAttribute("user", currentUser);
        } else {
            ///почему-то не пашет надо бы исправить
            AnotherUserProfile anotherUserProfile = userMapper.toAnotherUserProfile(user);
            model.addAttribute("user", user);
        }

        if (hasFriendRequest) {
            Long requestId = friendRequestService.getFriendRequestIdFromUser(user, currentUser);
            model.addAttribute("hasFriendRequest", true);
            model.addAttribute("requestId", requestId);
        } else {
            model.addAttribute("hasFriendRequest", false);
        }
        model.addAttribute("isSendRequestToUser", hasSendRequestToUser);
        model.addAttribute("hasFriendRequest", hasFriendRequest);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isFriend", isFriend);





        List<Object> principals = sessionRegistry.getAllPrincipals();
        List<String> usersNamesList = new ArrayList<>();
        for (Object principal: principals) {
            if (principal instanceof MyUserDetails) {
                usersNamesList.add(((MyUserDetails) principal).getUser().getLogin());
            }
        }

        model.addAttribute("activeUsers", usersNamesList);
        usersNamesList.clear();

        return "profile";
    }

}

