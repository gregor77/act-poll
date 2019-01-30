package com.act.poll;

import com.act.poll.chat.Chat;
import com.act.poll.chat.ChatService;
import com.act.poll.chat.User;
import com.act.poll.event.ChatEvent;
import com.act.poll.event.ChatEventHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ChatEventHandlerTest {
    private static final String ANY_CHAT_ROOM_ID = "any-room-id";
    private static final String ANY_MESSAGE = "any-message";
    private static final String ANY_WRITER_ID = "any-writer-id";
    private static final String ANY_WRITER_NAME = "any-writer-name";
    @InjectMocks
    private ChatEventHandler subject;
    @Mock
    private WebSocketSession mockSession;
    @Mock
    private ObjectMapper mockObjectMapper;
    @Mock
    private ChatService mockChatService;
    @Captor
    private ArgumentCaptor<TextMessage> textMessageCaptor;

    @Test
    public void whenAfterConnectEstablished_thenAddSessions() throws Exception {
        subject.afterConnectionEstablished(mockSession);

        Set<WebSocketSession> sessions = subject.getSessions();
        assertThat(sessions.contains(mockSession)).isTrue();
    }

    @Test
    public void givenValidChatRoomId_whenHandleTextMessage_thenAddSession() throws Exception {
        //given
        WebSocketSession mockSession1 = mock(WebSocketSession.class);
        WebSocketSession mockSession2 = mock(WebSocketSession.class);
        subject.afterConnectionEstablished(mockSession1);
        subject.afterConnectionEstablished(mockSession2);

        String jsonChatMessage = "{\n" +
                "  \"chatRoomId\": \"any-room-id\",\n" +
                "  \"writerId\": \"any-writer-id\",\n" +
                "  \"writerName\": \"any-writer-name\",\n" +
                "  \"message\": \"any-message\"\n" +
                "}";
        ChatEvent chatEvent = ChatEvent.builder()
                .chatRoomId(ANY_CHAT_ROOM_ID)
                .writerId(ANY_WRITER_ID)
                .writerName(ANY_WRITER_NAME)
                .message(ANY_MESSAGE)
                .build();
        when(mockObjectMapper.readValue(jsonChatMessage, ChatEvent.class))
            .thenReturn(chatEvent);
        when(mockObjectMapper.writeValueAsString(chatEvent)).thenReturn(jsonChatMessage);

        //when
        subject.handleTextMessage(mockSession, new TextMessage(jsonChatMessage));

        //then
        then(mockChatService).should().registerChat(Chat.builder()
                .message(ANY_MESSAGE)
                .writer(User.builder()
                        .id(ANY_WRITER_ID)
                        .name(ANY_WRITER_NAME)
                        .build())
                .chatRoomId(ANY_CHAT_ROOM_ID)
                .build());

        then(mockSession1).should().sendMessage(textMessageCaptor.capture());
        then(mockSession2).should().sendMessage(any(TextMessage.class));
        TextMessage textMessage = textMessageCaptor.getValue();
        assertThat(textMessage).isEqualTo(new TextMessage(jsonChatMessage));
    }

    @Test
    public void whenAfterConnectClosed_thenSessionRemoved() throws Exception {
        subject.afterConnectionClosed(mockSession, CloseStatus.NORMAL);

        Set<WebSocketSession> sessions = subject.getSessions();
        assertThat(sessions).doesNotContain(mockSession);
    }
}