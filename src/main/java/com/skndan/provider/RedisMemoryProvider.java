package com.skndan.provider;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;

import java.util.List;

/**
 * Provider for managing short-term memory storage using Redis.
 * Handles saving, retrieving, and clearing message histories for chat interactions.
 */
@ApplicationScoped
public class RedisMemoryProvider {

    @Inject
    RedissonClient redisson;

    private static final String PREFIX = "chat:memory:";

    /**
     * Saves a message to the user's memory list in Redis.
     * Maintains a rolling window of the most recent 20 messages.
     *
     * @param userId unique identifier for the user
     * @param message the message to be stored
     */
    public void saveMessage(String userId, String message) {
        String key = PREFIX + userId;
        RList<String> list = redisson.getList(key);
        list.add(message);

        // Keep only last 20 messages
        if (list.size() > 20) {
            list.trim(list.size() - 20, list.size() - 1);
        }
    }

    /**
     * Retrieves the entire message history for a specific user.
     *
     * @param userId unique identifier for the user
     * @return list of messages stored in the user's memory
     */
    public List<String> getMemory(String userId) {
        String key = PREFIX + userId;
        RList<String> list = redisson.getList(key, StringCodec.INSTANCE);
        return list.readAll();
    }

    /**
     * Clears the entire message history for a specific user.
     *
     * @param userId unique identifier for the user
     */
    public void clear(String userId) {
        String key = PREFIX + userId;
        redisson.getList(key).delete();
    }
}