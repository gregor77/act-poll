package com.act.poll.chatroom;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatRoomControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ChatRoomService mockChatRoomService;

    @Test
    public void whenCreateChatRoomWithChatRoomId_thenReturnChatroom() {
        ChatRoom anyChatRoom = ChatRoom.builder().name("any-chatroom").build();
        when(mockChatRoomService.create(anyChatRoom)).thenReturn(anyChatRoom);

        ResponseEntity<ChatRoom> responseEntity = restTemplate.postForEntity(
                "/chatroom",
                anyChatRoom,
                ChatRoom.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ChatRoom chatroom = responseEntity.getBody();
        assertThat(chatroom.getName()).isEqualTo("any-chatroom");
    }

    @Test
    public void whenGetAllChatRoom_thenReturnChatRoomList() {
        ChatRoom firstChatRoom = ChatRoom.builder().id("first").name("chatroom-1").build();
        ChatRoom secondChatRoom = ChatRoom.builder().id("second").name("chatroom-2").build();
        ChatRoom thirdChatRoom = ChatRoom.builder().id("third").name("chatroom-3").build();
        when(mockChatRoomService.getAll()).thenReturn(Lists.newArrayList(
                firstChatRoom, secondChatRoom, thirdChatRoom
        ));

        ResponseEntity<List<ChatRoom>> responseEntity = restTemplate.exchange(
                "/chatroom",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ChatRoom>>() {
                }

        );
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).contains(
                firstChatRoom,
                secondChatRoom,
                thirdChatRoom
        );
    }
}
