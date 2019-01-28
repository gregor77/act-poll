package com.act.poll.chatroom;

import com.act.poll.chat.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatDetail {
    private ChatRoom chatRoom;

    private List<Chat> messages;
}
