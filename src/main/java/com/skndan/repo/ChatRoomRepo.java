package com.skndan.repo;

import com.skndan.entity.ChatRoom;
import com.skndan.entity.Paged;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repository for managing ChatRoom entities.
 * Provides database operations for chat rooms with additional method to find rooms by user ID.
 */
@ApplicationScoped
public class ChatRoomRepo extends BaseRepo<ChatRoom, Long> implements PanacheRepository<ChatRoom> {
    /**
     * Retrieves a paginated list of chat rooms for a specific user.
     *
     * @param page the page number to retrieve
     * @param size the number of items per page
     * @param userId the unique identifier of the user
     * @return a Paged object containing the user's active chat rooms
     */
    public Paged<ChatRoom> findAllPagedById(int page, int size, String userId) {
        return findPaged("active = ?1 and userId = ?2", page, size, true, userId);
    }
}