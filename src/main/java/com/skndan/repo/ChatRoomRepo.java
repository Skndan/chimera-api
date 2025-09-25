package com.skndan.repo;

import com.skndan.entity.ChatRoom;
import com.skndan.entity.Paged;
import com.skndan.entity.Ticket;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ChatRoomRepo extends BaseRepo<ChatRoom, Long> implements PanacheRepository<ChatRoom> {
    public Paged<ChatRoom> findAllPagedById(int page, int size, String userId) {
        return findPaged("active = ?1 and userId = ?2", page, size, true, userId);
    }
}