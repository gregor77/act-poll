package com.act.poll.chat;

import com.act.poll.chatroom.ChatDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/chatroom")
public class ChatController {
    private ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/{id}/messages")
    public ChatDetail getChatDetail(@PathVariable(name = "id") String chatRoomId) {
        return chatService.getChatDetail(chatRoomId);
    }

    @PostMapping("/{id}/messages")
    public Chat registerChat(@PathVariable(name = "id") String chatRoomId,
                                 @RequestBody Chat chat) throws Exception {
        Optional.ofNullable(chatService.getChatDetail(chatRoomId))
                .orElseThrow(() -> new Exception("chat room is not found"));

        return chatService.registerChat(chatRoomId, chat);
    }

}
