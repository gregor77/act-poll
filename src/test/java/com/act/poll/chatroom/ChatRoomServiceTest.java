package com.act.poll.chatroom;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
public class ChatRoomServiceTest {
    private ChatRoom anyChatRoom;

    @InjectMocks
    private ChatRoomService subject;
    @Mock
    private ChatRoomRepository mockChatRoomRepository;
    @Captor
    private ArgumentCaptor<ChatRoom> chatRoomCaptor;

    @Before
    public void setUp() {
        anyChatRoom = ChatRoom.builder()
                .name("any-name")
                .build();
    }

    @Test
    public void whenCreateWithChatRoom_thenSaveChatRoom() {
        subject.create(anyChatRoom);

        then(mockChatRoomRepository).should().save(chatRoomCaptor.capture());
        ChatRoom chatRoom = chatRoomCaptor.getValue();
    }

    @Test
    public void whenGetAll_thenChatRoomList() {
        subject.getAll();

        then(mockChatRoomRepository).should().findAll();
    }
}