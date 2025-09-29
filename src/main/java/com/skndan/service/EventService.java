package com.skndan.service;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.BackPressureStrategy;
import io.smallrye.mutiny.subscription.MultiEmitter;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing real-time event streams using Server-Sent Events (SSE).
 * Handles registration of clients and notification of messages to specific chat rooms.
 */
@ApplicationScoped
public class EventService {
    private final Map<String, MultiEmitter<? super String>> emitters = new ConcurrentHashMap<>();

    /**
     * Registers a client to receive events for a specific chat room.
     * Creates an event stream that clients can subscribe to for real-time updates.
     *
     * @param roomId the unique identifier for the chat room
     * @return a stream of events that clients can listen to
     */
    public Multi<String> register(String roomId) {
        return Multi.createFrom().emitter(em -> {
            emitters.put(roomId, em);
        }, BackPressureStrategy.BUFFER);
    }

    /**
     * Sends a message to all clients subscribed to a specific chat room.
     * The message is formatted according to Server-Sent Events (SSE) standards.
     *
     * @param roomId the unique identifier for the chat room
     * @param message the content to send to subscribed clients
     */
    public void notify(String roomId, String message) {
        var em = emitters.get(roomId);
        if (em != null) {
            em.emit("data: " + message + "\n\n"); // SSE format
        }
    }
}