package com.act.poll.chatroom;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatRoomService {
    private ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public ChatRoom create(ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoom> getAll() {
        return chatRoomRepository.findAll();
    }
}
