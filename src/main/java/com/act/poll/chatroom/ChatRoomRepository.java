package com.act.poll.chatroom;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends CrudRepository<ChatRoom, String> {
    List<ChatRoom> findAll();
}
