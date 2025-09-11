package com.skndan.provider;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.*;
import io.quarkus.redis.client.RedisClient;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.list.ListCommands;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class RedisMemoryProvider {

    private final ListCommands<String, String> list;

    @Inject
    public RedisMemoryProvider(RedisDataSource ds) {
        this.list = ds.list(String.class);
    }

    private static final String PREFIX = "chat:memory:";

    public void saveMessage(String userId, String message) {
        String key = PREFIX + userId;
        list.rpush(key, message);
        list.ltrim(key, -20, -1); // keep only last 20 messages
    }

    public List<String> getMemory(String userId) {
        String key = PREFIX + userId;
        return list.lrange(key, 0, -1);
    }

//    public void clear(String userId) {
//        String key = PREFIX + userId;
//        list.del(key);
//    }
}