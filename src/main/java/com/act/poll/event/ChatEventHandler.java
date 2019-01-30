package com.act.poll.event;

import com.act.poll.chat.Chat;
import com.act.poll.chat.ChatRepository;
import com.act.poll.chat.ChatService;
import com.act.poll.chat.User;
import com.act.poll.chatroom.ChatRoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ChatEventHandler extends TextWebSocketHandler {
    private Set<WebSocketSession> sessions = new HashSet<>();   //broadcast를 위해

    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    public ChatEventHandler(ObjectMapper objectMapper, ChatService chatService) {
        this.objectMapper = objectMapper;
        this.chatService = chatService;
    }

    @Autowired


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //TODO. ws connection 언제 맺을 것인가 확인
        super.afterConnectionEstablished(session);
        log.debug("ws is connected." + session);
        sessions.add(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //TODO. 시나리오 확인
        // 채팅방 입장 후 메세지 전송시, ws 이벤트 전송, chat message data 생성
        super.handleTextMessage(session, message);

        String payload = message.getPayload();
        log.debug("ws message: " + payload);
        ChatEvent chatEvent = objectMapper.readValue(payload, ChatEvent.class);
        chatService.registerChat(Chat.builder()
                .chatRoomId(chatEvent.getChatRoomId())
                .message(chatEvent.getMessage())
                .writer(User.builder()
                        .id(chatEvent.getWriterId())
                        .name(chatEvent.getWriterName())
                        .build())
                .build());

        sessions.forEach(s -> {
            try {
                s.sendMessage(new TextMessage(payload));
            } catch (IOException e) {
                log.debug("send message throw error: " + e.getMessage());
            }
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        //TODO. client에서 chat room에서 나오는 순간 close하면 호출되는지 확인
        log.debug("ws is disconnected. " + session);
        sessions.remove(session);
    }

    public Set<WebSocketSession> getSessions() {
        return this.sessions;
    }
}
