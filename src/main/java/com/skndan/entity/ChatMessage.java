package com.skndan.entity;

import com.skndan.entity.constant.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
public class ChatMessage extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    public ChatRoom chatRoom;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    public Role sender; // "user" or "ai"

    @Column(columnDefinition = "TEXT")
    public String content;

    @Column(nullable = false)
    public Instant timestamp = Instant.now();
}