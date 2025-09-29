package com.skndan.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a chat room in the application.
 * Stores information about the room's owner and name.
 */
@Entity
@Getter
@Setter
public class ChatRoom extends BaseEntity {

    /**
     * The unique identifier of the user who owns this chat room.
     * Ensures that each chat room is associated with a specific user.
     */
    @Column(nullable = false)
    public String userId;

    /**
     * The name of the chat room.
     * Provides a human-readable identifier for the room.
     */
    @Column(nullable = false)
    public String name;

}