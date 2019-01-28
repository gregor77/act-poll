package com.act.poll.chat;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends CrudRepository<Chat, String> {
    List<Chat> findAllByChatRoomId(String chatRoomId);
}
