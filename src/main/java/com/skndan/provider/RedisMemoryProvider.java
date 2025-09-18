package com.skndan.provider;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;

import java.util.List;

@ApplicationScoped
public class RedisMemoryProvider {

    @Inject
    RedissonClient redisson;

    private static final String PREFIX = "chat:memory:";

    public void saveMessage(String userId, String message) {
        String key = PREFIX + userId;
        RList<String> list = redisson.getList(key);
        list.add(message);

        // Keep only last 20 messages
        if (list.size() > 20) {
            list.trim(list.size() - 20, list.size() - 1);
        }
    }

    public List<String> getMemory(String userId) {
        String key = PREFIX + userId;
        RList<String> list = redisson.getList(key, StringCodec.INSTANCE);
        return list.readAll();
    }

    public void clear(String userId) {
        String key = PREFIX + userId;
        redisson.getList(key).delete();
    }
}