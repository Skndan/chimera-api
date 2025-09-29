package com.skndan.entity;

import com.skndan.entity.constant.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Represents a message in a chat conversation.
 * Stores details about the sender, content, and associated chat room.
 */
@Entity
@Getter
@Setter
public class ChatMessage extends BaseEntity {

    /**
     * The chat room to which this message belongs.
     * Establishes a many-to-one relationship with the ChatRoom entity.
     */
    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    public ChatRoom chatRoom;

    /**
     * The sender of the message, represented as a Role enum.
     * Indicates whether the message is from a user or an AI.
     */
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    public Role sender; // "user" or "ai"

    /**
     * The textual content of the chat message.
     * Stored as a TEXT column to support longer message lengths.
     */
    @Column(columnDefinition = "TEXT")
    public String content;

    /**
     * The timestamp when the message was created.
     * Automatically set to the current instant when the message is instantiated.
     */
    @Column(nullable = false)
    public Instant timestamp = Instant.now();
}