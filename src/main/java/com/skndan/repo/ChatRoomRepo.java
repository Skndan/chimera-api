package com.skndan.repo;

import com.skndan.entity.ChatRoom;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ChatRoomRepo extends BaseRepo<ChatRoom, Long> implements PanacheRepository<ChatRoom> {
}