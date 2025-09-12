package com.skndan.service;

import com.skndan.ai.AiChimeraBot;
import com.skndan.entity.ChatMessage;
import com.skndan.entity.ChatRoom;
import com.skndan.entity.constant.Role;
import com.skndan.model.record.ChatRequest;
import com.skndan.model.record.LlmResponse;
import com.skndan.provider.RedisMemoryProvider;
import com.skndan.repo.ChatMessageRepo;
import com.skndan.repo.ChatRoomRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.SecurityContext;
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

    private static final Logger LOG = Logger.getLogger(ChatService.class);

    public LlmResponse chat(String userId, ChatRequest req) {

        LOG.info("---------------------START-----------------------");
        LOG.info("prompt: "+req.prompt());
        LOG.info("sheet: "+req.sheet());
        LOG.info("cell: "+req.cell());
        LOG.info("roomName: "+req.roomName());
        LOG.info("selectionType: "+req.selectionType());
        LOG.info("----------------------END------------------------");

        // Ensure ChatRoom exists (simplified)
        var room = roomRepo.find("userId = ?1 and name = ?2", userId, req.roomName())
                .firstResult();
        if (room == null) {
            room = new ChatRoom();
            room.userId = userId;
            room.name = req.roomName();
            roomRepo.persist(room);
        }

        // 1) Save user message into Postgres + Redis
        var userMsg = new ChatMessage();
        userMsg.chatRoom = room;
        userMsg.sender = Role.USER;
        userMsg.content = req.prompt();
        msgRepo.persist(userMsg);

        redisMemory.saveMessage(userId, "User: " + req.prompt());

        // 2) Get short-term memory from Redis and build a single string context
        List<String> mem = redisMemory.getMemory(userId); // List<String> like ["User: ...","AI: ..."]
        String memoryContext = buildMemoryContext(mem);

        // 3) Build the final model input (prompt + history)
        String inputForModel;
        if (memoryContext.isBlank()) {
            inputForModel = req.prompt();
        } else {
            inputForModel = req.prompt()
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
            llmResponse = bot.chat(req.selectionType(), req.cell(), req.range(), req.sheet(), inputForModel); // If Bot.chat(prompt) only
        } catch (Exception ex) {
            // handle LLM errors appropriately (log/retry/fallback)
            throw new RuntimeException("LLM call failed", ex);
        }

        // 5) Persist assistant reply (Postgres + Redis)
        if (llmResponse != null) {
            // Choose what to save: use payload.text() or payload.cell.value/formula depending on schema
            String assistantText = llmResponse.payload() != null ? llmResponse.payload().text() : null;
            if (assistantText == null) assistantText = "<no-text-from-llm>";

            var aiMsg = new ChatMessage();
            aiMsg.chatRoom = room;
            aiMsg.sender = Role.AI;
            aiMsg.content = assistantText;
            msgRepo.persist(aiMsg);

            redisMemory.saveMessage(userId, "AI: " + assistantText);
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