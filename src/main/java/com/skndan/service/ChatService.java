package com.skndan.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skndan.ai.AiChimeraBot;
import com.skndan.entity.ChatMessage;
import com.skndan.entity.ChatRoom;
import com.skndan.entity.constant.Role;
import com.skndan.model.record.LlmResponse;
import com.skndan.model.request.LlmRequest;
import com.skndan.provider.RedisMemoryProvider;
import com.skndan.repo.ChatMessageRepo;
import com.skndan.repo.ChatRoomRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class ChatService {

    @Inject
    RedisMemoryProvider redisMemory;

    @Inject
    ChatRoomRepo roomRepo;

    @Inject
    ChatMessageRepo msgRepo;

    @Inject
    AiChimeraBot bot; // LangChain4j generated AI service

    @Inject
    ObjectMapper objectMapper; // Quarkus provides this via CDI

    private static final Logger LOG = Logger.getLogger(ChatService.class);

    public LlmResponse chat(String roomId, LlmRequest req) {

        LOG.info("---------------------START-----------------------");
        LOG.info("prompt: " + req.toString());
        LOG.info("----------------------END------------------------");

        // Ensure ChatRoom exists (simplified)
        var room = roomRepo.find("id = ?1", roomId)
                .firstResult();
//
//        if (room == null) {
//            room = new ChatRoom();
//            room.userId = userId;
//            room.name = req.getRoomName();
//            roomRepo.persist(room);
//        }

        // 1) Save user message into Postgres + Redis
        var userMsg = new ChatMessage();
        userMsg.chatRoom = room;
        userMsg.sender = Role.USER;
        userMsg.content = req.toString();
        msgRepo.persist(userMsg);

        redisMemory.saveMessage(roomId, "User: " + req);

        // 2) Get short-term memory from Redis and build a single string context
        List<String> mem = redisMemory.getMemory(roomId); // List<String> like ["User: ...","AI: ..."]
        String memoryContext = buildMemoryContext(mem);


        // 3) Build the final model input (prompt + history)
        String inputForModel = req.toString();

        if (!memoryContext.isBlank()) {
            inputForModel = inputForModel
                    + "\n\nConversationHistory:\n"
                    + memoryContext;
        }

        // 4) Call the Bot (LLM) — two common signatures shown:
        // Option A: Bot.chat(@MemoryId String memoryId, String prompt)
        LlmResponse llmResponse = null;
        try {
            // If your Bot has a MemoryId parameter, pass userId so langchain4j ties memory
            // to this user (and the service can manage chat memory automatically).
            // Uncomment the correct call depending on your Bot interface.
            // llmResponse = bot.chat(userId, inputForModel); // If Bot.chat(memoryId, prompt)


            LOG.info("---------------------START-----------------------");
            LOG.info(inputForModel);
            LOG.info("----------------------END------------------------");
            // Option B: Bot.chat(String prompt) -> pass flattened context in the prompt
            llmResponse = bot.chat(inputForModel); // If Bot.chat(prompt) only
        } catch (Exception ex) {
            // handle LLM errors appropriately (log/retry/fallback)
            throw new RuntimeException("LLM call failed", ex);
        }

        // 5) Persist assistant reply (Postgres + Redis)
        if (llmResponse != null) {
            // Choose what to save: use payload.text() or payload.cell.value/formula depending on schema

            String assistantText = "<no-text-from-llm>";

            try {
                assistantText = objectMapper.writeValueAsString(llmResponse);
            } catch (JsonProcessingException jx) {
                LOG.error(jx.getMessage());
            }

//            String assistantText = llmResponse.payload() != null ? llmResponse.payload().text() : null;
//            if (assistantText == null) assistantText = "<no-text-from-llm>";

            var aiMsg = new ChatMessage();
            aiMsg.chatRoom = room;
            aiMsg.sender = Role.AI;
            aiMsg.content = assistantText;
            msgRepo.persist(aiMsg);

            redisMemory.saveMessage(roomId, "AI: " + assistantText);
        }

        // 6) Return structured LlmResponse (caller will forward to VSTO)
        return llmResponse;
    }

    private String buildMemoryContext(List<String> mem) {
        if (mem == null || mem.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (String line : mem) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
}