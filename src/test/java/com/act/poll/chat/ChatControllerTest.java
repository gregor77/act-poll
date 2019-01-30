package com.act.poll.chat;

import com.act.poll.chatroom.ChatDetail;
import com.act.poll.chatroom.ChatRoom;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatControllerTest {
    private static final String FIRST = "first";
    private static final String SECOND = "second";
    private static final String THIRD = "third";

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ChatService mockChatService;

    @Captor
    private ArgumentCaptor<Chat> chatCaptor;

    @Test
    public void whenGetChatDetail_thenReturnChatDetail() {
        Chat firstChat = Chat.builder().writer(User.builder().id(FIRST).name(FIRST).build()).message("first Chat").build();
        Chat secondChat = Chat.builder().writer(User.builder().id(SECOND).name(SECOND).build()).message("second Chat").build();
        Chat thirdChat = Chat.builder().writer(User.builder().id(THIRD).name(THIRD).build()).message("third Chat").build();
        when(mockChatService.getChatDetail("any-id"))
                .thenReturn(ChatDetail.builder()
                        .chatRoom(ChatRoom.builder().id("any-id").build())
                        .messages(Lists.newArrayList(firstChat, secondChat, thirdChat))
                        .build());

        ResponseEntity<ChatDetail> responseEntity = restTemplate.exchange(
                "/chatroom/any-id/messages",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ChatDetail>() {
                }
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        ChatDetail chatDetail = responseEntity.getBody();
        assertThat(chatDetail.getChatRoom().getId()).isEqualTo("any-id");
        assertThat(chatDetail.getMessages()).contains(firstChat, secondChat, thirdChat);
    }

    @Test
    public void givenNoChatRoomId_whenRegisterChat_thenReturnServerError() {
        when(mockChatService.getChatDetail("no-exist-id")).thenReturn(null);

        ResponseEntity<Exception> responseEntity = restTemplate.exchange(
                "/chatroom/no-exist-id/messages",
                HttpMethod.POST,
                new HttpEntity<>(ChatRoom.builder().build()),
                new ParameterizedTypeReference<Exception>() {
                }
        );
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(responseEntity.getBody().getMessage())
                .isEqualTo("chat room is not found");
    }

    @Test
    public void whenRegisterChat_thenAddChat() {
        when(mockChatService.getChatDetail("any-chatroom-id"))
                .thenReturn(ChatDetail.builder()
                        .chatRoom(ChatRoom.builder().id("any-id").build())
                        .build());

        restTemplate.postForEntity(
                "/chatroom/any-chatroom-id/messages",
                Chat.builder()
                        .writer(User.builder().id("any-writer-id").name("any-writer-name").build())
                        .message("any-Chat")
                        .build(),
                ChatRoom.class
        );

        then(mockChatService).should().registerChat(chatCaptor.capture());
        Chat chat = chatCaptor.getValue();
        assertThat(chat.getChatRoomId()).isEqualTo("any-chat-id");
        assertThat(chat.getWriter())
                .isEqualTo(User.builder().id("any-writer-id").name("any-writer-name").build());
        assertThat(chat.getMessage()).isEqualTo("any-Chat");
    }
}