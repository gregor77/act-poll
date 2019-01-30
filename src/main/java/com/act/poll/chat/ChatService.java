package com.act.poll.chat;

import com.act.poll.chatroom.ChatRoomRepository;
import com.act.poll.chatroom.ChatDetail;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    private ChatRoomRepository chatRoomRepository;
    private ChatRepository chatRepository;

    public ChatService(ChatRoomRepository chatRoomRepository, ChatRepository chatRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatRepository = chatRepository;
    }

    public ChatDetail getChatDetail(String chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .map(chatRoom -> {
                    List<Chat> chatList = chatRepository.findAllByChatRoomId(chatRoomId);
                    return ChatDetail.builder()
                            .chatRoom(chatRoom)
                            .messages(chatList)
                            .build();
                })
                .orElse(ChatDetail.builder()
                        .messages(new ArrayList<>())
                        .build());
    }

    public Chat registerChat(Chat chat) {
        return chatRepository.save(chat);
    }
}
