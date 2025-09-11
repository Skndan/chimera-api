package com.skndan.resource;

import com.skndan.ai.AiChimeraBot;
import com.skndan.ai.TriageService;
import com.skndan.model.record.ChatRequest;
import com.skndan.model.record.LlmResponse;
import com.skndan.model.record.TriagedReview;
import com.skndan.service.ChatService;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;

@Path("/review")
public class TriageResource {

//    public record ChatRequest(String roomName, String input) {}

    @Inject
    SecurityIdentity identity;


    @Inject
    TriageService triage;

    @Inject
    ChatService chatService;

    record Review(String review) {
    }

    @POST
    public TriagedReview triage(Review review) {
        return triage.triage(review.review());
    }


    @POST
    @Path("/chat")
    @Authenticated
    @Transactional
    public LlmResponse triage2(ChatRequest req) {

        String userId = identity.getPrincipal().getName();
        return chatService.chat(userId, req);
    }

}
