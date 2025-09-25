package com.skndan.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skndan.ai.AiChimeraBot;
import com.skndan.ai.TriageService;
import com.skndan.model.record.ChatRequest;
import com.skndan.model.record.LlmResponse;
import com.skndan.model.record.TriagedReview;
import com.skndan.model.request.LlmRequest;
import com.skndan.service.ChatService;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;

@Path("/ai")
public class AiResource {

//    public record ChatRequest(String roomName, String input) {}

    @Inject
    SecurityIdentity identity;

    @Inject
    ChatService chatService;

    record Review(String review) {
    }

    @POST
    @Path("/chat")
    @Authenticated
    @Transactional
    public LlmResponse chat(LlmRequest req) {
        String userId = identity.getPrincipal().getName();
        return chatService.chat(userId, req);
    }

}
