package com.skndan.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skndan.ai.ExcelBot;
import com.skndan.entity.ChatMessage;
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

/**
 * Service for managing chat interactions, including AI-powered conversations.
 * Handles message processing, storage, and interaction with AI services.
 */
@ApplicationScoped
public class ChatService {

    @Inject
    RedisMemoryProvider redisMemory;

    @Inject
    ChatRoomRepo roomRepo;

    @Inject
    ChatMessageRepo msgRepo;

    @Inject
    ExcelBot bot; // LangChain4j generated AI service

    @Inject
    ObjectMapper objectMapper; // Quarkus provides this via CDI

    @Inject
    EventService eventService;

    private static final Logger LOG = Logger.getLogger(ChatService.class);

    /**
     * Processes a chat message by integrating user input with AI-generated responses.
     * Manages conversation context, saves messages, and interacts with the AI service.
     *
     * @param roomId the unique identifier of the chat room
     * @param req the chat request containing user message details
     * @return an AI-generated response based on the conversation context
     * @throws RuntimeException if the AI service call fails
     */
    public LlmResponse chat(long roomId, LlmRequest req) {

        LOG.info("---------------------START-----------------------");
        LOG.info("prompt: " + req.toString());
        LOG.info("----------------------END------------------------");

        eventService.notify(String.valueOf(roomId), "status:RECEIVED");
//        eventService.notify(String.valueOf(roomId), "status:RECEIVED");
        // Ensure ChatRoom exists (simplified)
        var room = roomRepo.find("id = ?1", roomId)
                .firstResult();

        // 1) Save user message into Postgres + Redis
        var userMsg = new ChatMessage();
        userMsg.chatRoom = room;
        userMsg.sender = Role.USER;
        userMsg.content = req.toString();
        msgRepo.persist(userMsg);
//
//        redisMemory.saveMessage(String.valueOf(roomId), "User: " + req);
//
//        // 2) Get short-term memory from Redis and build a single string context
//        List<String> mem = redisMemory.getMemory(String.valueOf(roomId)); // List<String> like ["User: ...","AI: ..."]
//        String memoryContext = buildMemoryContext(mem);


        // 3) Build the final model input (prompt + history)
        String inputForModel = req.toString();

//        if (!memoryContext.isBlank()) {
//            inputForModel = inputForModel
//                    + "\n\nConversationHistory:\n"
//                    + memoryContext;
//        }

        eventService.notify(String.valueOf(roomId), "status:MEMORY_CHECKED");

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
            eventService.notify(String.valueOf(roomId), "status:GENERATING_RESPONSE");

            // Option B: Bot.chat(String prompt) -> pass flattened context in the prompt
            llmResponse = bot.chat(String.valueOf(roomId), inputForModel); // If Bot.chat(prompt) only
        } catch (Exception ex) {
            eventService.notify(String.valueOf(roomId), "status:ERROR");
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

//            redisMemory.saveMessage(String.valueOf(roomId), "AI: " + assistantText);

            eventService.notify(String.valueOf(roomId), "data:system:"+assistantText);
        }


        // 6) Return structured LlmResponse (caller will forward to VSTO)
        return llmResponse;
    }

    /**
     * Builds a conversation memory context from a list of previous messages.
     * Converts the memory list into a single string for AI context.
     *
     * @param mem list of previous conversation messages
     * @return a string representation of conversation history
     */
    private String buildMemoryContext(List<String> mem) {
        if (mem == null || mem.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (String line : mem) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
}