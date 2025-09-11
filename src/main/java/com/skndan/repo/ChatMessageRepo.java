package com.skndan.repo;

import com.skndan.entity.ChatMessage;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ChatMessageRepo extends BaseRepo<ChatMessage, Long> implements PanacheRepository<ChatMessage> {
}