package by.ryabchikov.coursework.controller;

import by.ryabchikov.coursework.model.user.ChatMessage;
import by.ryabchikov.coursework.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class ChatController {
    private final UserService userService;
    //private final ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        System.out.println(chatMessage.getType());
        System.out.println(chatMessage.getSender());
        System.out.println(chatMessage.getAvatar());
        System.out.println(chatMessage.getContent());
        //chatService.saveMessage(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        //headerAccessor.getSessionAttributes().put("username", userService.getCurrentUser().getName());
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    @GetMapping("/chat")
    public String getChatPage(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "chat";
    }
}



