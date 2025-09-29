package com.skndan.resource;

import com.skndan.model.record.LlmResponse;
import com.skndan.model.request.LlmRequest;
import com.skndan.service.ChatService;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/ai")
public class AiResource {

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
        return chatService.chat(req.getRoomId(), req);
    }

}
