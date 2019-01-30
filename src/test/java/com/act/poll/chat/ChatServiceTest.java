package com.act.poll.chat;

import com.act.poll.chatroom.ChatDetail;
import com.act.poll.chatroom.ChatRoom;
import com.act.poll.chatroom.ChatRoomRepository;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChatServiceTest {
    private static final String CHAT_ROOM_ID = "any-chatroom-id";
    private static final String FIRST = "first";
    private static final String SECOND = "second";
    private static final String THIRD = "third";

    @InjectMocks
    private ChatService subject;
    @Mock
    private ChatRepository mockChatRepository;
    @Mock
    private ChatRoomRepository mockChatRoomRepository;
    @Captor
    private ArgumentCaptor<Chat> chatCaptor;

    @Test
    public void givenFoundIsEmpty_whenGetChatRoomDetail_thenReturnEmptyChatRoom() {
        ChatDetail result = subject.getChatDetail(CHAT_ROOM_ID);
        assertThat(result).isEqualTo(ChatDetail.builder().messages(Lists.newArrayList()).build());
    }

    @Test
    public void whenGetChatDetail_thenReturnChatRoomAndChatList() {
        User firstUser = User.builder().id(FIRST).name(FIRST).build();
        User secondUser = User.builder().id(SECOND).name(SECOND).build();
        User thirdUser = User.builder().id(THIRD).name(THIRD).build();

        when(mockChatRoomRepository.findById(CHAT_ROOM_ID))
                .thenReturn(Optional.of(ChatRoom.builder()
                        .id("any-chatroom-id")
                        .name("any-chatroom-name")
                        .build()));
        when(mockChatRepository.findAllByChatRoomId(CHAT_ROOM_ID))
                .thenReturn(Lists.newArrayList(
                        Chat.builder()
                                .id(CHAT_ROOM_ID)
                                .writer(firstUser)
                                .message(FIRST)
                                .build(),
                        Chat.builder()
                                .id(CHAT_ROOM_ID)
                                .writer(secondUser)
                                .message(SECOND)
                                .build(),
                        Chat.builder()
                                .id(CHAT_ROOM_ID)
                                .writer(thirdUser)
                                .message(THIRD)
                                .build()
                ));

        ChatDetail result = subject.getChatDetail(CHAT_ROOM_ID);
        assertThat(result.getChatRoom())
                .isEqualTo(ChatRoom.builder().id(CHAT_ROOM_ID).name("any-chatroom-name").build());
        assertThat(result.getMessages()).contains(
                Chat.builder()
                        .id(CHAT_ROOM_ID)
                        .writer(firstUser)
                        .message(FIRST)
                        .build(),
                Chat.builder()
                        .id(CHAT_ROOM_ID)
                        .writer(secondUser)
                        .message(SECOND)
                        .build(),
                Chat.builder()
                        .id(CHAT_ROOM_ID)
                        .writer(thirdUser)
                        .message(THIRD)
                        .build()
        );
    }

    @Test
    public void whenRegisterChat_thenSaveChat() {
        subject.registerChat(
                Chat.builder()
                        .chatRoomId("any-chatroom-id")
                        .writer(User.builder().id("any-user-id").name("any-user-name").build())
                        .message("any-message")
                        .build()
        );

        then(mockChatRepository).should().save(chatCaptor.capture());
        Chat chat = chatCaptor.getValue();
        assertThat(chat.getWriter()).isEqualTo(User.builder().id("any-user-id").name("any-user-name").build());
        assertThat(chat.getMessage()).isEqualTo("any-message");
    }
}