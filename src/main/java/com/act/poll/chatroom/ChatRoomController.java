package com.act.poll.chatroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatroom")
public class ChatRoomController {

    private ChatRoomService chatRoomService;

    @Autowired
    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChatRoom createChatRoom(@RequestBody ChatRoom chatRoom) {
        return chatRoomService.create(chatRoom);
    }

    @GetMapping
    public List<ChatRoom> getAll() {
        return chatRoomService.getAll();
    }
}
